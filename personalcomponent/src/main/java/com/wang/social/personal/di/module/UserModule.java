package com.wang.social.personal.di.module;

import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:21
 * ========================================
 */
@Module
public class UserModule {

    @FragmentScope
    @Provides
    UserModule provideUserModel(UserModule model) {
        return model;
    }
}