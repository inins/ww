package com.wang.social.topic.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.topic.mvp.contract.ReleaseTopicContract;

import javax.inject.Inject;

@ActivityScope
public class ReleaseTopicModel extends BaseModel implements ReleaseTopicContract.Model {

    @Inject
    public ReleaseTopicModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }
    
}
