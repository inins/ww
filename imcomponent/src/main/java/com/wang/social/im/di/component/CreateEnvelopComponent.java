package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.CreateEnvelopModule;
import com.wang.social.im.mvp.ui.CreateMultiEnvelopActivity;
import com.wang.social.im.mvp.ui.CreateSingleEnvelopActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-24 11:10
 * ============================================
 */
@ActivityScope
@Component(modules = CreateEnvelopModule.class, dependencies = AppComponent.class)
public interface CreateEnvelopComponent {

    void inject(CreateSingleEnvelopActivity activity);

    void inject(CreateMultiEnvelopActivity activity);
}