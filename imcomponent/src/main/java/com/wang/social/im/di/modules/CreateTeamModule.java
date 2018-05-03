package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.CreateTeamContract;
import com.wang.social.im.mvp.model.CreateTeamModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 15:20
 * ============================================
 */
@Module
public class CreateTeamModule {

    CreateTeamContract.View view;

    public CreateTeamModule(CreateTeamContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CreateTeamContract.View provideTeamHomeView(){
        return view;
    }

    @ActivityScope
    @Provides
    CreateTeamContract.Model provideTeamHomeModel(CreateTeamModel model){
        return model;
    }
}
