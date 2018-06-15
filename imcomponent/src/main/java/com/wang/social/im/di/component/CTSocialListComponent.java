package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.CTSocialListModule;
import com.wang.social.im.mvp.ui.CTSocialListActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-06-14 10:14
 * ============================================
 */
@ActivityScope
@Component(modules = CTSocialListModule.class, dependencies = AppComponent.class)
public interface CTSocialListComponent {

    void inject(CTSocialListActivity activity);
}
