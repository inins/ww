package com.wang.social.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.mvp.ui.activity.HomeActivity;

import dagger.Component;

/**
 * ============================================
 *
 * @link android.app.Activity} 通用Component
 * <p>
 * Create by ChenJing on 2018-04-23 11:19
 * ============================================
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent {

    void inject(HomeActivity activity);
}