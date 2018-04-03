package com.wang.social.personal.di.component;

import android.app.Activity;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.frame.integration.IRepositoryManager;
import com.wang.social.personal.di.module.UserModule;

import dagger.Component;
import dagger.Provides;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:19
 * ========================================
 */
@ActivityScope
@Component(modules = UserModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {

    IRepositoryManager repoitoryManager();

    void inject(Activity activity);
}