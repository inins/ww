package com.wang.social.topic.di.module;

import com.frame.di.scope.ActivityScope;
import com.wang.social.topic.mvp.contract.ReleaseTopicContract;
import com.wang.social.topic.mvp.model.ReleaseTopicModel;
import com.wang.social.topic.mvp.model.TemplateModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ReleaseTopicModule {

    private ReleaseTopicContract.View view;

    public ReleaseTopicModule(ReleaseTopicContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ReleaseTopicContract.View provideReleaseTopicView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ReleaseTopicContract.Model provideReleaseTopicModel(ReleaseTopicModel model) {
        return model;
    }
}
