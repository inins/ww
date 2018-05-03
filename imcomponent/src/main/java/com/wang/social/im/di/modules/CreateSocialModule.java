package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.CreateSocialContract;
import com.wang.social.im.mvp.model.CreateSocialModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 15:20
 * ============================================
 */
@Module
public class CreateSocialModule {

    CreateSocialContract.View view;

    public CreateSocialModule(CreateSocialContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CreateSocialContract.View provideTeamHomeView(){
        return view;
    }

    @ActivityScope
    @Provides
    CreateSocialContract.Model provideTeamHomeModel(CreateSocialModel model){
        return model;
    }
}
