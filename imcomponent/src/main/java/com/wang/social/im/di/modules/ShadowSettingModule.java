package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.ShadowSettingContract;
import com.wang.social.im.mvp.model.ShadowSettingModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 17:40
 * ============================================
 */
@Module
public class ShadowSettingModule {

    private ShadowSettingContract.View view;

    public ShadowSettingModule(ShadowSettingContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ShadowSettingContract.View provideShadowSettingView(){
        return view;
    }

    @ActivityScope
    @Provides
    ShadowSettingContract.Model provideShadowSettingModel(ShadowSettingModel model){
        return model;
    }
}
