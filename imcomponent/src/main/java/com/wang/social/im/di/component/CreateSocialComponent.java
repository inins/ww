package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.CreateSocialModule;
import com.wang.social.im.di.modules.TeamHomeModule;
import com.wang.social.im.mvp.ui.CreateSocialActivity;
import com.wang.social.im.mvp.ui.TeamHomeActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 15:22
 * ============================================
 */
@ActivityScope
@Component(modules = CreateSocialModule.class, dependencies = AppComponent.class)
public interface CreateSocialComponent {

    void inject(CreateSocialActivity activity);
}
