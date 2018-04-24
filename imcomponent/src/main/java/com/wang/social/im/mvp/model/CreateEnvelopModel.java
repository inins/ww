package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.CreateEnvelopContract;

import javax.inject.Inject;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-24 10:04
 * ============================================
 */
@ActivityScope
public class CreateEnvelopModel extends BaseModel implements CreateEnvelopContract.Model {

    @Inject
    public CreateEnvelopModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }
}
