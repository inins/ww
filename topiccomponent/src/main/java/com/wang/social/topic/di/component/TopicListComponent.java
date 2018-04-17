package com.wang.social.topic.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.FragmentScope;
import com.wang.social.topic.di.module.TopicListModule;
import com.wang.social.topic.mvp.ui.fragment.TopicListFragment;

import dagger.Component;

@FragmentScope
@Component(modules = TopicListModule.class, dependencies = AppComponent.class)
public interface TopicListComponent {
    void inject(TopicListFragment topicListFragment);
}
