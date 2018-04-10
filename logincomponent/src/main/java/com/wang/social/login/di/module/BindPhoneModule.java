package com.wang.social.login.di.module;

import com.frame.di.scope.ActivityScope;
import com.wang.social.login.mvp.contract.BindPhoneContract;
import com.wang.social.login.mvp.model.BindPhoneModel;

import dagger.Module;
import dagger.Provides;

@Module
public class BindPhoneModule {

    private BindPhoneContract.View view;

    public BindPhoneModule(BindPhoneContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    BindPhoneContract.View provideBindPhoneView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    BindPhoneContract.Model provideBindPhoneModel(BindPhoneModel model) {
        return model;
    }
}
