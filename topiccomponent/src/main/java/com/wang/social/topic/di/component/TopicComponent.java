package com.wang.social.topic.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.wang.social.topic.di.module.TopicModule;
import com.wang.social.topic.mvp.ui.TopUserActivity;
import com.wang.social.topic.mvp.ui.fragment.TopUserFragment;
import com.wang.social.topic.mvp.ui.fragment.TopicFragment;

import dagger.Component;

@FragmentScope
@Component(modules = TopicModule.class, dependencies = AppComponent.class)
public interface TopicComponent {
    void inject(TopicFragment topicFragment);
    void inject(TopUserFragment topUserFragment);
}
