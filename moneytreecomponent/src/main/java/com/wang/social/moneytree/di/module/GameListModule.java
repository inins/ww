package com.wang.social.moneytree.di.module;

import com.frame.di.scope.ActivityScope;
import com.wang.social.moneytree.mvp.contract.GameListContract;
import com.wang.social.moneytree.mvp.model.GameListModel;

import dagger.Module;
import dagger.Provides;

@Module
public class GameListModule {

    private GameListContract.View view;

    public GameListModule(GameListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GameListContract.View provideGameListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GameListContract.Model provideGameListModel(GameListModel model) {
        return model;
    }
}
