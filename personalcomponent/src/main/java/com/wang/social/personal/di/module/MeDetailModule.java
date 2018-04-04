package com.wang.social.personal.di.module;

import com.frame.di.scope.ActivityScope;
import com.wang.social.personal.mvp.contract.MeDetailContract;
import com.wang.social.personal.mvp.model.MeDetailModel;

import dagger.Module;
import dagger.Provides;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:21
 * ========================================
 */
@Module
public class MeDetailModule {

    private MeDetailContract.View view;

    public MeDetailModule(MeDetailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MeDetailContract.View provideView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    MeDetailContract.Model provideModel(MeDetailModel model) {
        return model;
    }
}