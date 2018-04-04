package com.wang.social.login.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.login.di.module.ResetPasswordModule;
import com.wang.social.login.mvp.ui.ResetPasswordActivity;

import dagger.Component;

@ActivityScope
@Component(modules = ResetPasswordModule.class, dependencies = AppComponent.class)
public interface ResetPasswordComponent {
    void inject(ResetPasswordActivity resetPasswordActivity);
}
