package com.wang.social.topic.di.module;

import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.wang.social.topic.mvp.contract.TopicDetailContract;
import com.wang.social.topic.mvp.model.TopicDetailModel;
import com.wang.social.topic.mvp.model.TopicModel;

import dagger.Module;
import dagger.Provides;

@Module
public class TopicDetailModule {

    private TopicDetailContract.View view;

    public TopicDetailModule(TopicDetailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    TopicDetailContract.View provideTopicDetailView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    TopicDetailContract.Model provideTopicDetailModel(TopicDetailModel model) {
        return model;
    }
}
