package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.CreateSocialContract;

import javax.inject.Inject;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 20:25
 * ============================================
 */
@ActivityScope
public class CreateSocialModel extends BaseModel implements CreateSocialContract.Model{

    @Inject
    public CreateSocialModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }
}
