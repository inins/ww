package com.wang.social.im.mvp.model;

import com.frame.di.scope.FragmentScope;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.ConversationContract;

import javax.inject.Inject;

/**
 * ======================================
 * <p>
 * Create by ChenJing on 2018-04-03 16:43
 * ======================================
 */
@FragmentScope
public class ConversationModel extends BaseModel implements ConversationContract.Model{

    @Inject
    public ConversationModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }
}