package com.wang.social.im.app;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.frame.base.delegate.AppDelegate;
import com.frame.di.module.GlobalConfigModule;
import com.frame.integration.ConfigModule;

import java.util.List;

/**
 * ======================================
 * <p>
 * Create by ChenJing on 2018-04-04 14:18
 * ======================================
 */
public class IMConfiguration implements ConfigModule {

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {

    }

    @Override
    public void injectAppLifecycle(Context context, List<AppDelegate> lifecycles) {
        lifecycles.add(new ImAppLifecycleImpl());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycleCallbacks) {

    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycleCallbacks) {

    }
}
