package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.FragmentScope;
import com.wang.social.im.di.modules.FriendsModule;
import com.wang.social.im.mvp.ui.fragments.FriendsFragment;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-16 20:14
 * ============================================
 */
@FragmentScope
@Component(modules = FriendsModule.class, dependencies = AppComponent.class)
public interface FriendsComponent {

    void inject(FriendsFragment fragment);
}
