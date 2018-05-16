package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.GroupConversationContract;

import javax.inject.Inject;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-15 13:49
 * ============================================
 */
@ActivityScope
public class GroupConversationPresenter extends BasePresenter<GroupConversationContract.Model, GroupConversationContract.View> {

    @Inject
    public GroupConversationPresenter(GroupConversationContract.Model model, GroupConversationContract.View view) {
        super(model, view);
    }
}
