package com.wang.social.im.mvp.presenter;

import android.support.annotation.NonNull;

import com.frame.di.scope.FragmentScope;
import com.frame.mvp.BasePresenter;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
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

    public void setConversation(@NonNull TIMConversation conversation) {
        this.mConversation = conversation;
        this.mConversationExt = new TIMConversationExt(mConversation);
    }

    public void getHistoryMessage() {
        TIMMessage lastMessage = null;
        if (mAdapter != null && mAdapter.getData().size() > 0) {
            lastMessage = mAdapter.getData().get(mAdapter.getItemCount() - 1).getTimMessage();
        }
        mConversationExt.getMessage(20, lastMessage, new TIMValueCallBack<List<TIMMessage>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMMessage> timMessages) {
                if (timMessages == null) {
                    return;
                }
                List<UIMessage> uiMessages = new ArrayList<>();
                for (TIMMessage message : timMessages) {
                    uiMessages.add(UIMessage.obtain(message));
                }
                mRootView.showMessages(uiMessages);
            }
        });
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

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        if (list != null) {
            mRootView.insertMessages(UIMessage.obtain(list));
        }
        return false;
    }
}