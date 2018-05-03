package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.CreateSocialModule;
import com.wang.social.im.di.modules.CreateTeamModule;
import com.wang.social.im.mvp.ui.CreateSocialActivity;
import com.wang.social.im.mvp.ui.CreateTeamActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 15:22
 * ============================================
 */
@ActivityScope
@Component(modules = CreateTeamModule.class, dependencies = AppComponent.class)
public interface CreateTeamComponent {

    void inject(CreateTeamActivity activity);
}
