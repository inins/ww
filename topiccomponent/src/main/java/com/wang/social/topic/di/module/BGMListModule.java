package com.wang.social.topic.di.module;

import com.frame.di.scope.ActivityScope;
import com.wang.social.topic.mvp.contract.BGMListContract;
import com.wang.social.topic.mvp.model.BGMListModel;

import dagger.Module;
import dagger.Provides;

@Module
public class BGMListModule {

    private BGMListContract.View view;

    public BGMListModule(BGMListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    BGMListContract.View provideBGMListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    BGMListContract.Model provideBGMListModel(BGMListModel model) {
        return model;
    }
}
