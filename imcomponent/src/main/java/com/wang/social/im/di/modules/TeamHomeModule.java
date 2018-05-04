package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.TeamHomeContract;
import com.wang.social.im.mvp.model.TeamHomeModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 15:20
 * ============================================
 */
@Module
public class TeamHomeModule {

    TeamHomeContract.View view;

    public TeamHomeModule(TeamHomeContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    TeamHomeContract.View provideTeamHomeView(){
        return view;
    }

    @ActivityScope
    @Provides
    TeamHomeContract.Model provideTeamHomeModel(TeamHomeModel model){
        return model;
    }
}
