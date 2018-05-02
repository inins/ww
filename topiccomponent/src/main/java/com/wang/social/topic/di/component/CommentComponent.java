package com.wang.social.topic.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.topic.di.module.CommentModule;
import com.wang.social.topic.mvp.ui.CommentActivity;

import dagger.Component;

@ActivityScope
@Component(modules = CommentModule.class, dependencies = AppComponent.class)
public interface CommentComponent {
    void inject(CommentActivity commentActivity);
}
