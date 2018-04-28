package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.EnvelopDetailModule;
import com.wang.social.im.mvp.ui.EnvelopDetailActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-28 10:02
 * ============================================
 */
@ActivityScope
@Component(modules = EnvelopDetailModule.class, dependencies = AppComponent.class)
public interface EnvelopDetailComponent {

    void inject(EnvelopDetailActivity activity);
}
