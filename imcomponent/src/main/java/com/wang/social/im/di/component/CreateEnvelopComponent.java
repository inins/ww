package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.wang.social.im.di.modules.CreateEnvelopModule;
import com.wang.social.im.mvp.ui.CreateSingleEnvelopActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-24 11:10
 * ============================================
 */
@Component(modules = CreateEnvelopModule.class, dependencies = AppComponent.class)
public interface CreateEnvelopComponent {

    void inject(CreateSingleEnvelopActivity activity);
}
