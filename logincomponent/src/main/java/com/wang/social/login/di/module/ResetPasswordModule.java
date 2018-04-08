package com.wang.social.login.di.module;

import com.frame.di.scope.ActivityScope;
import com.wang.social.login.mvp.contract.ResetPasswordContract;
import com.wang.social.login.mvp.model.ResetPasswordModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ResetPasswordModule {

    private ResetPasswordContract.View view;

    public ResetPasswordModule(ResetPasswordContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ResetPasswordContract.View provideResetPasswordView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ResetPasswordContract.Model provideResetPasswordModel(ResetPasswordModel model) {
        return model;
    }
}
