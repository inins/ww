package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.BackgroundSettingModule;
import com.wang.social.im.mvp.ui.BackgroundSettingActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-24 9:23
 * ============================================
 */
@ActivityScope
@Component(modules = BackgroundSettingModule.class, dependencies = AppComponent.class)
public interface BackgroundSettingComponent {

    void inject(BackgroundSettingActivity activity);
}
