package com.wang.social.login.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.login.mvp.contract.ResetPasswordContract;

import javax.inject.Inject;


@ActivityScope
public class ResetPasswordModel extends BaseModel implements ResetPasswordContract.Model {
    @Inject
    public ResetPasswordModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

}
