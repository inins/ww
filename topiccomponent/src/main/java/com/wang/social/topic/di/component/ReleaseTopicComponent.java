package com.wang.social.topic.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.topic.di.module.ReleaseTopicModule;
import com.wang.social.topic.di.module.TemplateModule;
import com.wang.social.topic.mvp.ui.ReleaseTopicActivity;
import com.wang.social.topic.mvp.ui.TemplateActivity;

import dagger.Component;

@ActivityScope
@Component(modules = ReleaseTopicModule.class, dependencies = AppComponent.class)
public interface ReleaseTopicComponent {
    void inject(ReleaseTopicActivity releaseTopicActivity);
}
