package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.BackgroundSettingContract;
import com.wang.social.im.mvp.model.BackgroundSettingModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-24 9:21
 * ============================================
 */
@Module
public class BackgroundSettingModule {

    BackgroundSettingContract.View view;

    public BackgroundSettingModule(BackgroundSettingContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    BackgroundSettingContract.View provideView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    BackgroundSettingContract.Model provideModel(BackgroundSettingModel model) {
        return model;
    }
}
