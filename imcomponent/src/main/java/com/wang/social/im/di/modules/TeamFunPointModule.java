package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.FunPointContract;
import com.wang.social.im.mvp.model.FunPointModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-17 10:52
 * ============================================
 */
@Module
public class TeamFunPointModule {

    FunPointContract.View view;

    public TeamFunPointModule(FunPointContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    FunPointContract.View provideView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FunPointContract.Model provideModel(FunPointModel model) {
        return model;
    }
}
