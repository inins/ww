package com.wang.social.im.mvp.presenter;

import android.support.annotation.NonNull;

import com.frame.di.scope.FragmentScope;
import com.frame.entities.EventBean;
import com.frame.mvp.BasePresenter;
import com.google.gson.Gson;
import com.tencent.imcore.CustomElem;
import com.tencent.imcore.GroupManager;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMFaceElem;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.enums.CustomElemType;
import com.wang.social.im.enums.GameNotifyType;
import com.wang.social.im.enums.MessageType;
import com.wang.social.im.mvp.contract.GameConversationContract;
import com.wang.social.im.mvp.model.entities.GameNotifyElemData;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.mvp.ui.adapters.MessageListAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-11 11:02
 * ============================================
 */
@FragmentScope
public class GameConversationPresenter extends BasePresenter<GameConversationContract.Model, GameConversationContract.View> implements TIMMessageListener {

    private MessageListAdapter mAdapter;
    private TIMConversation mConversation;
    private TIMConversationExt mConversationExt;

    @Inject
    Gson gson;

    @Inject
    public GameConversationPresenter(GameConversationContract.Model model, GameConversationContract.View view) {
        super(model, view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mConversation = null;
        mConversationExt = null;
        mAdapter = null;
        TIMManager.getInstance().removeMessageListener(this);
    }

    public void setAdapter(MessageListAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    /**
     * 设置会话对象
     *
     * @param conversation
     */
    public void setConversation(@NonNull TIMConversation conversation) {
        this.mConversation = conversation;
        this.mConversationExt = new TIMConversationExt(mConversation);
    }

    /**
     * 加入群组
     *
     * @param identity
     */
    public void joinGroup(String identity) {
        TIMGroupManager.getInstance().applyJoinGroup(identity, "", new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                if (i == IMConstants.TIM_ERROR_CODE_GROUP_JOINED) {
                    TIMManager.getInstance().addMessageListener(GameConversationPresenter.this);

                    mRootView.joinComplete();
                    getHistoryMessage();
                }
            }

            @Override
            public void onSuccess() {
                TIMManager.getInstance().addMessageListener(GameConversationPresenter.this);

                mRootView.joinComplete();
                getHistoryMessage();
            }
        });
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        if (mConversation == null) {
            return false;
        }
        if (list != null && list.size() > 0) {
            if (list.get(0).getConversation().getType() == mConversation.getType() &&
                    list.get(0).getConversation().getPeer().equals(mConversation.getPeer())) {
                List<UIMessage> uiMessages = new ArrayList<>();
                for (int i = list.size() - 1; i >= 0; i--) {
                    TIMMessage message = list.get(i);
                    UIMessage uiMessage = UIMessage.obtain(message);
                    if (uiMessage.getMessageType() == MessageType.GAME_NOTIFY) {
                        GameNotifyElemData elemData = (GameNotifyElemData) uiMessage.getCustomMessageElemData(CustomElemType.GAME_NOTIFY, GameNotifyElemData.class, gson);
                        if (elemData.getNotifyType() == GameNotifyType.JOIN) {
                            EventBean event = new EventBean(EventBean.EVENT_GAME_JOIN);
                            event.put("joinNumber", elemData.getJoinPersonNum());
                            EventBus.getDefault().post(event);
                        } else if (elemData.getNotifyType() == GameNotifyType.RESULT) {
                            EventBus.getDefault().post(new EventBean(EventBean.EVENT_GAME_RESULT));
                            continue;
                        }
                    }
                    uiMessages.add(uiMessage);
                }
                if (uiMessages.size() > 0) {
                    mRootView.showMessages(uiMessages);
                }
            }
        }
        return false;
    }

    /**
     * 获取历史消息
     */
    public void getHistoryMessage() {
        if (mConversationExt == null) {
            return;
        }
        TIMMessage lastMessage = null;
        if (mAdapter != null && mAdapter.getData() != null && mAdapter.getData().size() > 0) {
            lastMessage = mAdapter.getData().get(0).getTimMessage();
        }
        mConversationExt.getLocalMessage(30, lastMessage, new TIMValueCallBack<List<TIMMessage>>() {
            @Override
            public void onError(int i, String s) {
                mRootView.hideLoading();
            }

            @Override
            public void onSuccess(List<TIMMessage> timMessages) {
                mRootView.hideLoading();
                if (timMessages == null) {
                    return;
                }
                List<UIMessage> uiMessages = new ArrayList<>();
                for (int i = timMessages.size() - 1; i >= 0; i--) {
                    TIMMessage message = timMessages.get(i);
                    UIMessage uiMessage = UIMessage.obtain(message);
                    if (uiMessage.getMessageType() == MessageType.GAME_NOTIFY) {
                        GameNotifyElemData elemData = (GameNotifyElemData) uiMessage.getCustomMessageElemData(CustomElemType.GAME_NOTIFY, GameNotifyElemData.class, gson);
                        if (elemData.getNotifyType() == GameNotifyType.RESULT) {
                            continue;
                        }
                    }
                    uiMessages.add(uiMessage);
                }
                mRootView.insertMessages(uiMessages);
            }
        });
    }

    /**
     * 发送一条文本消息
     *
     * @param content
     */
    public void sendTextMessage(String content) {
        TIMMessage message = new TIMMessage();
        TIMTextElem textElem = new TIMTextElem();
        textElem.setText(content);
        message.addElement(textElem);

        doSendMessage(message);
    }

    /**
     * 发送一条表情消息
     */
    public void sendFaceMessage(int index) {
        TIMMessage message = new TIMMessage();
        TIMFaceElem faceElem = new TIMFaceElem();
        faceElem.setIndex(index);
        message.addElement(faceElem);

        doSendMessage(message);
    }

    /**
     * 执行发送
     *
     * @param message
     */
    private void doSendMessage(TIMMessage message) {
        if (mConversation == null) {
            return;
        }
        mConversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(TIMMessage timMessage) {

            }
        });
        mRootView.showMessage(UIMessage.obtain(message));
    }
}
