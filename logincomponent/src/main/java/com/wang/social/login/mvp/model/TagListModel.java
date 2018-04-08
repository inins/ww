package com.wang.social.login.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.login.mvp.contract.TagListContract;

import javax.inject.Inject;

@ActivityScope
public class TagListModel extends BaseModel implements TagListContract.Model {
    @Inject
    public TagListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }
}
