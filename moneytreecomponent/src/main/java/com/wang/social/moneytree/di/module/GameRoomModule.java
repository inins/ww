package com.wang.social.moneytree.di.module;

import com.frame.di.scope.ActivityScope;
import com.wang.social.moneytree.mvp.contract.GameRoomContract;
import com.wang.social.moneytree.mvp.model.GameRoomModel;

import dagger.Module;
import dagger.Provides;

@Module
public class GameRoomModule {

    private GameRoomContract.View view;

    public GameRoomModule(GameRoomContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GameRoomContract.View provideGameRoomView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GameRoomContract.Model provideGameRoomModel(GameRoomModel model) {
        return model;
    }
}
