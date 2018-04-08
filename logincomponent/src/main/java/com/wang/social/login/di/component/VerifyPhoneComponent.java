package com.wang.social.login.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.login.di.module.VerifyPhoneModule;
import com.wang.social.login.mvp.ui.VerifyPhoneActivity;

import dagger.Component;

@ActivityScope
@Component(modules = VerifyPhoneModule.class, dependencies = AppComponent.class)
public interface VerifyPhoneComponent {
    void inject(VerifyPhoneActivity verifyPhoneActivity);
}
