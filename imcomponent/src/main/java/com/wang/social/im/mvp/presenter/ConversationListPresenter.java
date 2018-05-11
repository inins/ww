package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.FragmentScope;
import com.frame.mvp.BasePresenter;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.mvp.contract.ConversationListContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-16 20:09
 * ============================================
 */
@FragmentScope
public class ConversationListPresenter extends BasePresenter<ConversationListContract.Model, ConversationListContract.View> implements TIMMessageListener {

    @Inject
    public ConversationListPresenter(ConversationListContract.Model model, ConversationListContract.View view) {
        super(model, view);

        TIMManager.getInstance().addMessageListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TIMManager.getInstance().removeMessageListener(this);
    }

    /**
     * 获取会话列表
     */
    public void getConversationList() {
        List<TIMConversation> conversations = TIMManagerExt.getInstance().getConversationList();
        List<TIMConversation> result = new ArrayList<>();
        for (TIMConversation conversation : conversations) {
            if ((conversation.getType() != TIMConversationType.Group && conversation.getType() != TIMConversationType.C2C) ||
                    conversation.getPeer().startsWith(IMConstants.IM_IDENTITY_PREFIX_MIRROR) ||
                    conversation.getPeer().startsWith(IMConstants.IM_IDENTITY_PREFIX_GAME)) {
                continue;
            }
            result.add(conversation);
            TIMConversationExt conversationExt = new TIMConversationExt(conversation);
            conversationExt.getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    Timber.tag(TAG).w("Conversation get message error," + s);
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    if (timMessages != null && timMessages.size() > 0) {
                        mRootView.updateMessage(timMessages.get(0));
                    }
                }
            });
        }
        mRootView.initList(result);
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        mRootView.updateMessages(list);
        return false;
    }
}
