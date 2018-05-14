package com.wang.social.moneytree.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.moneytree.di.module.GameRecordListModule;
import com.wang.social.moneytree.mvp.ui.GameRecordListActivity;

import dagger.Component;

@ActivityScope
@Component(modules = GameRecordListModule.class, dependencies = AppComponent.class)
public interface GameRecordListComponent {
    void inject(GameRecordListActivity gameRecordListActivity);
}
