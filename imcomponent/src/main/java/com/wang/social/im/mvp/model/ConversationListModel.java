package com.wang.social.im.mvp.model;

import com.frame.di.scope.FragmentScope;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.ConversationListContract;

import javax.inject.Inject;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-16 20:08
 * ============================================
 */
@FragmentScope
public class ConversationListModel extends BaseModel implements ConversationListContract.Model{

    @Inject
    public ConversationListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }
}