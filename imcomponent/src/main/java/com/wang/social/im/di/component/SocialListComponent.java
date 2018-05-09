package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.SocialListModule;
import com.wang.social.im.mvp.ui.SocialListActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 9:58
 * ============================================
 */
@ActivityScope
@Component(modules = SocialListModule.class, dependencies = AppComponent.class)
public interface SocialListComponent {

    void inject(SocialListActivity activity);
}
