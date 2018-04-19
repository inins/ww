package com.wang.social.topic.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.topic.di.module.TopicDetailModule;
import com.wang.social.topic.mvp.ui.TopicDetailActivity;

import dagger.Component;

@ActivityScope
@Component(modules = TopicDetailModule.class, dependencies = AppComponent.class)
public interface TopicDetailComponent {
    void inject(TopicDetailActivity topicDetailActivity);
}
