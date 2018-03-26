package com.frame.base;

import android.support.annotation.NonNull;

import com.frame.di.component.AppComponent;

/**
 * ======================================
 * 框架要求每个 {@link android.app.Application} 都实现此类， 以满足规范
 * <p>
 * Create by ChenJing on 2018-03-12 15:28
 * ======================================
 */

public interface App {

    @NonNull
    AppComponent getAppComponent();
}
