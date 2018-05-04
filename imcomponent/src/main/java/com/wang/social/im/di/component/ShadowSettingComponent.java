package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.ShadowSettingModule;
import com.wang.social.im.mvp.ui.ShadowSettingActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 17:42
 * ============================================
 */
@ActivityScope
@Component(modules = ShadowSettingModule.class, dependencies = AppComponent.class)
public interface ShadowSettingComponent {

    void inject(ShadowSettingActivity activity);
}
