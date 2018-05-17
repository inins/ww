package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.TeamFunPointModule;
import com.wang.social.im.mvp.ui.TeamFunPointActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-17 10:54
 * ============================================
 */
@ActivityScope
@Component(modules = TeamFunPointModule.class, dependencies = AppComponent.class)
public interface TeamFunPointComponent {

    void inject(TeamFunPointActivity activity);
}
