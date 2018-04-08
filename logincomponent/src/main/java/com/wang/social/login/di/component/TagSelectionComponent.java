package com.wang.social.login.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.login.di.module.TagSelectionModule;
import com.wang.social.login.mvp.ui.TagSelectionActivity;

import dagger.Component;

@ActivityScope
@Component(modules = TagSelectionModule.class, dependencies = AppComponent.class)
public interface TagSelectionComponent {
    void inject(TagSelectionActivity TagSelectionActivity);
}
