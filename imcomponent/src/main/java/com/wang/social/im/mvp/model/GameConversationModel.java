package com.wang.social.im.mvp.model;

import com.frame.di.scope.FragmentScope;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.GameConversationContract;

import javax.inject.Inject;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-11 11:00
 * ============================================
 */
@FragmentScope
public class GameConversationModel extends BaseModel implements GameConversationContract.Model {

    @Inject
    public GameConversationModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }
}