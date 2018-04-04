package com.wang.social.login.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.login.di.module.ForgotPasswordModule;
import com.wang.social.login.mvp.ui.ForgotPasswordActivity;

import dagger.Component;

@ActivityScope
@Component(modules = ForgotPasswordModule.class, dependencies = AppComponent.class)
public interface ForgotPasswordComponent {
    void inject(ForgotPasswordActivity forgotPasswordActivity);
}
