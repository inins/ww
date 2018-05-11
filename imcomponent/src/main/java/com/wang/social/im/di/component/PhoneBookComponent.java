package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.PhoneBookModule;
import com.wang.social.im.mvp.ui.PhoneBookActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-24 11:10
 * ============================================
 */
@ActivityScope
@Component(modules = PhoneBookModule.class, dependencies = AppComponent.class)
public interface PhoneBookComponent {

    void inject(PhoneBookActivity activity);
}