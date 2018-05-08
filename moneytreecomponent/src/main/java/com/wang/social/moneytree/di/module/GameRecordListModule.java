package com.wang.social.moneytree.di.module;

import com.frame.di.scope.ActivityScope;
import com.wang.social.moneytree.mvp.contract.GameRecordListContract;
import com.wang.social.moneytree.mvp.model.GameRecordListModel;

import dagger.Module;
import dagger.Provides;

@Module
public class GameRecordListModule {

    private GameRecordListContract.View view;

    public GameRecordListModule(GameRecordListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GameRecordListContract.View provideGameRecordListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GameRecordListContract.Model provideGameRecordListModel(GameRecordListModel model) {
        return model;
    }
}
