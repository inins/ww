package com.wang.social.personal.di.module;

import com.frame.di.scope.ActivityScope;
import com.wang.social.personal.mvp.contract.LableContract;
import com.wang.social.personal.mvp.contract.MeDetailContract;
import com.wang.social.personal.mvp.model.LableModel;
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
public class LableModule {

    private LableContract.View view;

    public LableModule(LableContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    LableContract.View provideView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    LableContract.Model provideModel(LableModel model) {
        return model;
    }
}