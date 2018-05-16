package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.wang.social.im.di.modules.NobodyModule;
import com.wang.social.im.mvp.ui.fragments.NobodyFragment;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-16 10:02
 * ============================================
 */
@FragmentScope
@Component(modules = NobodyModule.class, dependencies = AppComponent.class)
public interface NobodyComponent {

    void inject(NobodyFragment fragment);
}