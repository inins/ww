package com.wang.social.login.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.login.di.module.BindPhoneModule;
import com.wang.social.login.mvp.ui.BindPhoneActivity;

import dagger.Component;

@ActivityScope
@Component(modules = BindPhoneModule.class, dependencies = AppComponent.class)
public interface BindPhoneComponent {
    void inject(BindPhoneActivity bindPhoneActivity);
}
