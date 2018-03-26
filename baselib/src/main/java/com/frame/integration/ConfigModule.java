package com.frame.integration;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.frame.base.delegate.AppDelegate;
import com.frame.di.module.GlobalConfigModule;

import java.util.List;

/**
 * =================================
 * 给框架配置一些参数，需要实现{@link ConfigModule} 后， 在 AndroidManifest文件中声明
 * <p>
 * Create by ChenJing on 2018-03-14 13:25
 * =================================
 */

public interface ConfigModule {

    /**
     * 使用{@link GlobalConfigModule.Builder} 给框架配置一些配置参数
     * @param context
     * @param builder
     */
    void applyOptions(Context context, GlobalConfigModule.Builder builder);

    /**
     * 使用{@link AppDelegate} 在Application的生命周期中注入一些操作
     * @param context
     * @param lifecycles
     */
    void injectAppLifecycle(Context context, List<AppDelegate> lifecycles);

    /**
     * 使用{@link Application.ActivityLifecycleCallbacks} 在{@link android.app.Activity} 生命周期中注入一些操作
     * @param context
     * @param lifecycleCallbacks
     */
    void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycleCallbacks);

    /**
     * 使用{@link FragmentManager.FragmentLifecycleCallbacks} 在{@link android.support.v4.app.Fragment} 生命周期中注入一些操作
     * @param context
     * @param lifecycleCallbacks
     */
    void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycleCallbacks);
}
