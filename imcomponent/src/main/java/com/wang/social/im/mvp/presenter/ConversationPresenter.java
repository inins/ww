package com.wang.social.im.mvp.presenter;

import android.support.annotation.NonNull;

import com.frame.di.scope.FragmentScope;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.wang.social.im.mvp.contract.ConversationContract;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.mvp.ui.adapters.MessageListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mConversation = null;
        mAdapter = null;
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
     * 发送一条文本消息
     *
     * @param content
     */
    public void sendTextMessage(String content) {
        TIMMessage message = new TIMMessage();
        TIMTextElem textElem = new TIMTextElem();
        textElem.setText(content);
        message.addElement(textElem);
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
    }
}