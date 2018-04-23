package com.wang.social.funshow.di.module;

import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.wang.social.funshow.mvp.contract.FunshowListContract;
import com.wang.social.funshow.mvp.model.FunshowListModel;

import dagger.Module;
import dagger.Provides;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:21
 * ========================================
 */
@Module
public class FunshowListModule {

    private FunshowListContract.View view;

    public FunshowListModule(FunshowListContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    FunshowListContract.View provideView(){
        return this.view;
    }

    @FragmentScope
    @Provides
    FunshowListContract.Model provideModel(FunshowListModel model) {
        return model;
    }
}