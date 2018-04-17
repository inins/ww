package com.wang.social.funshow.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;

import dagger.Component;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:19
 * ========================================
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface SingleActivityComponent {
//    void inject(SearchActivity activity);
}