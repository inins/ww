package com.wang.social.im.mvp.presenter;

import android.support.annotation.NonNull;

import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.google.gson.Gson;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMLocationElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMSoundElem;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.wang.social.im.R;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.enums.CustomElemType;
import com.wang.social.im.mvp.contract.ConversationContract;
import com.wang.social.im.mvp.model.entities.EnvelopElemData;
import com.wang.social.im.mvp.model.entities.EnvelopInfo;
import com.wang.social.im.mvp.model.entities.EnvelopMessageCacheInfo;
import com.wang.social.im.mvp.model.entities.LocationAddressInfo;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.mvp.ui.adapters.MessageListAdapter;
import com.wang.social.location.mvp.model.entities.LocationInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * ======================================
 * <p>
 * Create by ChenJing on 2018-04-03 16:44
 * ======================================
 */
@FragmentScope
public class ConversationPresenter extends BasePresenter<ConversationContract.Model, ConversationContract.View> implements TIMMessageListener {

    private MessageListAdapter mAdapter;
    private TIMConversation mConversation;
    private TIMConversationExt mConversationExt;

    @Inject
    Gson gson;
    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public ConversationPresenter(ConversationContract.Model model, ConversationContract.View view) {
        super(model, view);

        TIMManager.getInstance().addMessageListener(this);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mConversation = null;
        mConversationExt = null;
        mAdapter = null;
        gson = null;
        TIMManager.getInstance().removeMessageListener(this);
    }

    /**
     * 接收到新消息
     *
     * @param list
     * @return
     */
    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        if (list != null && list.size() > 0) {
            if (list.get(0).getConversation().getType() == mConversation.getType() &&
                    list.get(0).getConversation().getPeer().equals(mConversation.getPeer())) {
                mRootView.showMessages(UIMessage.obtain(list));
            }
        }
        return false;
    }

    /**
     * 获取历史消息
     */
    public void getHistoryMessage() {
        TIMMessage lastMessage = null;
        if (mAdapter != null && mAdapter.getData() != null && mAdapter.getData().size() > 0) {
            lastMessage = mAdapter.getData().get(mAdapter.getItemCount() - 1).getTimMessage();
        }
        mConversationExt.getLocalMessage(20, lastMessage, new TIMValueCallBack<List<TIMMessage>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMMessage> timMessages) {
                if (timMessages == null) {
                    return;
                }
                mRootView.insertMessages(UIMessage.obtain(timMessages));
            }
        });
    }

    /**
     * 删除一条消息
     *
     * @param uiMessage
     */
    public void deleteMessage(UIMessage uiMessage) {
        if (doDeleteMessage(uiMessage)) {
            int position = mAdapter.findPosition(uiMessage);
            mAdapter.removeItem(position);
        }
    }

    /**
     * 撤回一条消息
     *
     * @param uiMessage
     */
    public void withDrawMessage(UIMessage uiMessage) {
        mConversationExt.revokeMessage(uiMessage.getTimMessage(), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                if (i == IMConstants.TIM_ERROR_CODE_REVOKE_TIMEOUT) {
                    ToastUtil.showToastShort(mRootView.getContext().getString(R.string.im_toast_revoke_timeout));
                }
            }

            @Override
            public void onSuccess() {
                uiMessage.refresh();
                mRootView.refreshMessage(uiMessage);
            }
        });
    }

    /**
     * 将消息状态改为已读
     */
    public void readMessages() {
        mConversationExt.setReadMessage(null, null);
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
     * 发送一条语音消息
     *
     * @param soundElem
     */
    public void sendSoundMessage(TIMSoundElem soundElem) {
        TIMMessage message = new TIMMessage();
        message.addElement(soundElem);

        doSendMessage(message);
    }

    /**
     * 发送图片消息
     *
     * @param images
     */
    public void sendImageMessage(String[] images, boolean isOri) {
        for (String path : images) {
            TIMMessage timMessage = new TIMMessage();
            TIMImageElem imageElem = new TIMImageElem();
            imageElem.setLevel(isOri ? 0 : 1);
            imageElem.setPath(path);
            timMessage.addElement(imageElem);

            doSendMessage(timMessage);
        }
    }

    /**
     * 发送红包消息
     *
     * @param envelopId
     * @param message
     */
    public void sendEnvelopMessage(long envelopId, String message) {
        TIMMessage timMessage = new TIMMessage();
        TIMCustomElem envelopElem = new TIMCustomElem();
        EnvelopElemData elemData = new EnvelopElemData();
        elemData.setEnvelopId(envelopId);
        elemData.setMessage(message);
        envelopElem.setData(gson.toJson(elemData).getBytes());
        timMessage.addElement(envelopElem);

        doSendMessage(timMessage);
    }

    /**
     * 发送位置消息
     *
     * @param locationInfo
     */
    public void sendLocationMessage(LocationInfo locationInfo) {
        TIMMessage timMessage = new TIMMessage();
        TIMLocationElem locationElem = new TIMLocationElem();
        locationElem.setLatitude(locationInfo.getLatitude());
        locationElem.setLongitude(locationInfo.getLongitude());
        LocationAddressInfo addressInfo = new LocationAddressInfo();
        addressInfo.setPlace(locationInfo.getPlace());
        addressInfo.setAddress(locationInfo.getAddress());
        locationElem.setDesc(gson.toJson(addressInfo));
        timMessage.addElement(locationElem);

        doSendMessage(timMessage);
    }

    /**
     * 获取红包详情
     *
     * @param uiMessage
     */
    public void getEnvelopInfo(UIMessage uiMessage) {
        EnvelopElemData envelopElemData = (EnvelopElemData) uiMessage.getCustomMessageElemData(CustomElemType.RED_ENVELOP, gson);
        if (envelopElemData != null) {
            mApiHelper.execute(mRootView, mModel.getEnvelopInfo(envelopElemData.getEnvelopId()), new ErrorHandleSubscriber<EnvelopInfo>(mErrorHandler) {
                @Override
                public void onNext(EnvelopInfo envelopInfo) {
                    mRootView.showEnvelopDialog(uiMessage, envelopInfo);
                    TIMMessageExt messageExt = new TIMMessageExt(uiMessage.getTimMessage());
                    //检查红包状态
                    if (envelopInfo.getGotDiamond() > 0){
                        messageExt.setCustomInt(EnvelopMessageCacheInfo.STATUS_ADOPTED);
                    }else {
                        switch (envelopInfo.getStatus()) {
                            case LIVING:
                                messageExt.setCustomInt(EnvelopMessageCacheInfo.STATUS_INITIAL);
                                break;
                            case EMPTY:
                                messageExt.setCustomInt(EnvelopMessageCacheInfo.STATUS_EMPTY);
                                break;
                            case OVERDUE:
                                messageExt.setCustomInt(EnvelopMessageCacheInfo.STATUS_OVERDUE);
                                break;
                        }
                    }
                    mRootView.refreshMessage(uiMessage);
                }
            }, new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mRootView.showLoading();
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mRootView.hideLoading();
                }
            });
        }
    }

    /**
     * 执行发送
     *
     * @param message
     */
    private void doSendMessage(TIMMessage message) {
        // TODO: 2018-04-25 判断是否需要添加匿名/分身消息标识
        mConversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                mRootView.refreshMessage(UIMessage.obtain(message));
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                mRootView.refreshMessage(UIMessage.obtain(timMessage));
            }
        });
        mRootView.showMessage(UIMessage.obtain(message));

        EventBus.getDefault().post(message);
    }

    /**
     * 执行删除消息
     *
     * @param uiMessage
     * @return
     */
    private boolean doDeleteMessage(UIMessage uiMessage) {
        TIMMessageExt messageExt = new TIMMessageExt(uiMessage.getTimMessage());
        return messageExt.remove();
    }

    /**
     * 插入一条信息到本地库
     *
     * @param timMessage
     */
    private int insertLocalMessage(TIMMessage timMessage) {
        return mConversationExt.saveMessage(timMessage, TIMManager.getInstance().getLoginUser(), false);
    }
}