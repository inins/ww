package com.wang.social.funpoint.di.module;

import com.frame.di.scope.FragmentScope;
import com.wang.social.funpoint.mvp.contract.FunpointListContract;
import com.wang.social.funpoint.mvp.model.FunpointListModel;

import dagger.Module;
import dagger.Provides;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:21
 * ========================================
 */
@Module
public class FunpointListModule {

    private FunpointListContract.View view;

    public FunpointListModule(FunpointListContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    FunpointListContract.View provideView(){
        return this.view;
    }

    @FragmentScope
    @Provides
    FunpointListContract.Model provideModel(FunpointListModel model) {
        return model;
    }
}