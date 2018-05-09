package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.MemberListModule;
import com.wang.social.im.mvp.ui.MemberListActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 20:07
 * ============================================
 */
@ActivityScope
@Component(modules = MemberListModule.class, dependencies = AppComponent.class)
public interface MemberListComponent {

    void inject(MemberListActivity activity);
}
