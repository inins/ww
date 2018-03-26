package com.wang.social.login.di.module;

import com.wang.social.login.mvp.contract.LoginContract;
import com.wang.social.login.mvp.model.LoginModel;
import com.frame.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:21
 * ========================================
 */
@Module
public class LoginModule {

    private LoginContract.View view;

    public LoginModule(LoginContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    LoginContract.View provideLoginView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    LoginContract.Model provideUserModel(LoginModel model) {
        return model;
    }
}