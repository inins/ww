package com.wang.social.topic.di.module;

import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.wang.social.topic.mvp.contract.TopicContract;
import com.wang.social.topic.mvp.model.TopicModel;

import dagger.Module;
import dagger.Provides;

@Module
public class TopicModule {

    private TopicContract.View view;

    public TopicModule(TopicContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    TopicContract.View provideTopicView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    TopicContract.Model provideTopicModel(TopicModel model) {
        return model;
    }
}
