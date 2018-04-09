package com.wang.social.personal.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.personal.di.module.LableModule;
import com.wang.social.personal.di.module.MeDetailModule;
import com.wang.social.personal.di.module.UserModule;
import com.wang.social.personal.mvp.ui.activity.LableActivity;
import com.wang.social.personal.mvp.ui.activity.MeDetailActivity;

import dagger.Component;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:19
 * ========================================
 */
@ActivityScope
@Component(modules = {LableModule.class}, dependencies = AppComponent.class)
public interface LableActivityComponent {
    void inject(LableActivity activity);
}