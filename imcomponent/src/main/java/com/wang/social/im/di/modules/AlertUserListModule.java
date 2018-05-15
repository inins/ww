package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.AlertUserListContract;
import com.wang.social.im.mvp.model.AlertUserListModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-12 17:23
 * ============================================
 */
@Module
public class AlertUserListModule {

    private AlertUserListContract.View view;

    public AlertUserListModule(AlertUserListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AlertUserListContract.View provideView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AlertUserListContract.Model provideModel(AlertUserListModel model) {
        return model;
    }
}