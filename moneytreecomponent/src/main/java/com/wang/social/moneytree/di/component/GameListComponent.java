package com.wang.social.moneytree.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.moneytree.di.module.GameListModule;
import com.wang.social.moneytree.mvp.ui.GameListActivity;

import dagger.Component;

@ActivityScope
@Component(modules = GameListModule.class, dependencies = AppComponent.class)
public interface GameListComponent {
    void inject(GameListActivity gameListActivity);
}
