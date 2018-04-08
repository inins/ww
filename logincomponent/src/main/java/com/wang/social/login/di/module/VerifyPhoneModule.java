package com.wang.social.login.di.module;

import com.frame.di.scope.ActivityScope;
import com.wang.social.login.mvp.contract.VerifyPhoneContract;
import com.wang.social.login.mvp.model.VerifyPhoneModel;

import dagger.Module;
import dagger.Provides;

@Module
public class VerifyPhoneModule {

    private VerifyPhoneContract.View view;

    public VerifyPhoneModule(VerifyPhoneContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    VerifyPhoneContract.View provideVerifyCodeView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    VerifyPhoneContract.Model provideVerifyCodeModel(VerifyPhoneModel model) {
        return model;
    }
}
