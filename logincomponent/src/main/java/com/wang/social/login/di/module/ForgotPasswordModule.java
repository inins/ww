package com.wang.social.login.di.module;

import com.frame.di.scope.ActivityScope;
import com.wang.social.login.mvp.contract.ForgotPasswordContract;
import com.wang.social.login.mvp.model.ForgotPasswordModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ForgotPasswordModule {

    private ForgotPasswordContract.View view;

    public ForgotPasswordModule(ForgotPasswordContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ForgotPasswordContract.View provideForgotPasswordView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ForgotPasswordContract.Model provideForgotPasswordModel(ForgotPasswordModel model) {
        return model;
    }
}
