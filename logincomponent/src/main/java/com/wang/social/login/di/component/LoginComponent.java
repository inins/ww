package com.wang.social.login.di.component;

import com.wang.social.login.di.module.LoginModule;
import com.wang.social.login.mvp.ui.LoginActivity;
import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;

import dagger.Component;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:19
 * ========================================
 */
@ActivityScope
@Component(modules = LoginModule.class, dependencies = AppComponent.class)
public interface LoginComponent {

    void inject(LoginActivity loginActivity);
}