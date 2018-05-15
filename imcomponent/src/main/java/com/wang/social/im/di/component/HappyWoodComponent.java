package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.HappyWoodModule;
import com.wang.social.im.mvp.ui.HappyWoodActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-14 17:37
 * ============================================
 */
@ActivityScope
@Component(modules = HappyWoodModule.class, dependencies = AppComponent.class)
public interface HappyWoodComponent {

    void inject(HappyWoodActivity activity);
}