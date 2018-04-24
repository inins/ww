package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.ui.PrivateConversationActivity;

import dagger.Component;

/**
 * ============================================
 * @link android.app.Activity} 通用Component
 * <p>
 * Create by ChenJing on 2018-04-23 11:19
 * ============================================
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent {

    void inject(PrivateConversationActivity activity);
}