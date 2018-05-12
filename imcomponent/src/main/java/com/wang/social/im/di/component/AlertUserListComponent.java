package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.AlertUserListModule;
import com.wang.social.im.mvp.ui.AlertUserListActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-12 17:24
 * ============================================
 */
@ActivityScope
@Component(modules = AlertUserListModule.class, dependencies = AppComponent.class)
public interface AlertUserListComponent {

    void inject(AlertUserListActivity activity);
}
