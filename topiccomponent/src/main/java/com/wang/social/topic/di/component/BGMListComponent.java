package com.wang.social.topic.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.topic.di.module.BGMListModule;
import com.wang.social.topic.mvp.ui.BGMListActivity;

import dagger.Component;

@ActivityScope
@Component(modules = BGMListModule.class, dependencies = AppComponent.class)
public interface BGMListComponent {
    void inject(BGMListActivity bgmListActivity);
}
