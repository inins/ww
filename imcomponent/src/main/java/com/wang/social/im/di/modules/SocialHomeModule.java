package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.SocialHomeContract;
import com.wang.social.im.mvp.model.SocialHomeModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-28 16:48
 * ============================================
 */
@Module
public class SocialHomeModule {

    private SocialHomeContract.View view;

    public SocialHomeModule(SocialHomeContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SocialHomeContract.View provideSocialHomeView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    SocialHomeContract.Model provideSocialHomeModel(SocialHomeModel model){
        return model;
    }
}