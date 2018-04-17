package com.wang.social.topic.di.module;

import com.frame.di.scope.FragmentScope;
import com.wang.social.topic.mvp.contract.TopicListContract;
import com.wang.social.topic.mvp.model.TopicListModel;
import com.wang.social.topic.mvp.model.TopicModel;

import dagger.Module;
import dagger.Provides;

@Module
public class TopicListModule {

    private TopicListContract.View view;

    public TopicListModule(TopicListContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    TopicListContract.View provideTopicListView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    TopicListContract.Model provideTopicListModel(TopicListModel model) {
        return model;
    }
}
