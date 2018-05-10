package com.wang.social.moneytree.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.moneytree.di.module.GameRoomModule;
import com.wang.social.moneytree.mvp.ui.GameRoomActivity;

import dagger.Component;

@ActivityScope
@Component(modules = GameRoomModule.class, dependencies = AppComponent.class)
public interface GameRoomComponent {
    void inject(GameRoomActivity gameRoomActivity);
}
