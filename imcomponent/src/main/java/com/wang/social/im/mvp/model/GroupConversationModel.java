package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.GroupConversationContract;

import javax.inject.Inject;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-15 13:48
 * ============================================
 */
@ActivityScope
public class GroupConversationModel extends BaseModel implements GroupConversationContract.Model{

    @Inject
    public GroupConversationModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }
}
