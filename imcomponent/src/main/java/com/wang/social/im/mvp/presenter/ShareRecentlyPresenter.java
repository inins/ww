package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.mvp.BasePresenter;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.mvp.contract.ShareRecentlyContract;
import com.wang.social.im.mvp.model.entities.UIConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.wang.social.im.app.IMConstants.SERVER_PUSH_MESSAGE_ACCOUNT;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-25 11:15
 * ============================================
 */
@ActivityScope
public class ShareRecentlyPresenter extends BasePresenter {

    @Inject
    public ShareRecentlyPresenter() {
    }

    public void setRootView(ShareRecentlyContract.View view) {
        this.mRootView = view;
        onStart();
    }

    /**
     * 获取最近联系人列表
     */
    public void getRecentlyContactList() {
        List<TIMConversation> conversations = TIMManagerExt.getInstance().getConversationList();
        List<UIConversation> result = new ArrayList<>();
        for (TIMConversation conversation : conversations) {
            if ((conversation.getType() != TIMConversationType.Group && conversation.getType() != TIMConversationType.C2C) ||
                    conversation.getPeer().startsWith(IMConstants.IM_IDENTITY_PREFIX_MIRROR) ||
                    conversation.getPeer().startsWith(IMConstants.IM_IDENTITY_PREFIX_GAME) ||
                    (conversation.getType() == TIMConversationType.C2C && conversation.getPeer().equals(SERVER_PUSH_MESSAGE_ACCOUNT))) {
                continue;
            }
            result.add(new UIConversation(conversation));
        }
        Collections.sort(result);
        ((ShareRecentlyContract.View) mRootView).showContacts(result);
    }
}
