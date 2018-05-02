package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.SocialHomeModule;
import com.wang.social.im.mvp.ui.SocialHomeActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-28 16:51
 * ============================================
 */
@ActivityScope
@Component(modules = SocialHomeModule.class, dependencies = AppComponent.class)
public interface SocialHomeComponent {

//    void inject(SocialHomeActivity activity);
}
