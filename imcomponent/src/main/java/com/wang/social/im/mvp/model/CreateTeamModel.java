package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.CreateTeamContract;

import javax.inject.Inject;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-03 14:06
 * ============================================
 */
@ActivityScope
public class CreateTeamModel extends BaseModel implements CreateTeamContract.Model{

    @Inject
    public CreateTeamModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }
}
