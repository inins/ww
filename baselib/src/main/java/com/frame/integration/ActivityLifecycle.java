package com.frame.integration;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.frame.base.delegate.ActivityDelegate;
import com.frame.base.delegate.ActivityDelegateImp;
import com.frame.base.delegate.IActivity;
import com.frame.integration.cache.Cache;
import com.frame.utils.Preconditions;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;

/**
 * ======================================
 * {@link Application.ActivityLifecycleCallbacks} 默认实现类
 * 通过{@link com.frame.base.delegate.ActivityDelegate} 管理 {@link Activity}
 * <p>
 * Create by ChenJing on 2018-03-12 15:44
 * ======================================
 */
@Singleton
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    Cache<String, Object> mExtras;
    @Inject
    Lazy<FragmentManager.FragmentLifecycleCallbacks> mFragmentLifecycle;
    @Inject
    Lazy<List<FragmentManager.FragmentLifecycleCallbacks>> mFragmentLifecycles;

    @Inject
    public ActivityLifecycle() {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        boolean isNotAdd = false;
        if (activity.getIntent() != null) {
            isNotAdd = activity.getIntent().getBooleanExtra(AppManager.IS_NOT_ADD_ACTIVITY_LIST, false);
        }
        if (!isNotAdd) {
            mAppManager.addActivity(activity);
        }
        if (activity instanceof IActivity) {
            ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
            if (activityDelegate == null) {
                Cache<String, Object> cache = getCacheFromActivity((IActivity) activity);
                activityDelegate = new ActivityDelegateImp(activity);
                cache.put(ActivityDelegate.ACTIVITY_DELEGATE_CACHE_KEY, activityDelegate);
            }
            activityDelegate.onCreate(savedInstanceState);
        }
        registerFragmentCallbacks(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onStart();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mAppManager.setCurrentActivity(activity);

        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onResume();
        }

        //清除通知
        ((NotificationManager) mApplication.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE)).cancel(521);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onPause();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (mAppManager.getCurrentActivity() == activity) {
            mAppManager.setCurrentActivity(null);
        }
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onStop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mAppManager.removeActivity(activity);

        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onDestroy();
            getCacheFromActivity((IActivity) activity).clear();
        }
    }

    private void registerFragmentCallbacks(Activity activity) {
        boolean useFragment = activity instanceof IActivity ? ((IActivity) activity).useFragment() : true;
        if (activity instanceof FragmentActivity && useFragment) {

            //mFragmentLifecycle 为 Fragment 的生命周期实现类
            ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycle.get(), true);

            if (mExtras.containsKey(ConfigModule.class.getName())) {
                List<ConfigModule> modules = (List<ConfigModule>) mExtras.get(ConfigModule.class.getName());
                for (ConfigModule module : modules) {
                    module.injectFragmentLifecycle(mApplication, mFragmentLifecycles.get());
                }
                mExtras.remove(ConfigModule.class.getName());
            }

            //注册框架外部，扩展的 Fragment 生命周期逻辑
            for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks : mFragmentLifecycles.get()) {
                ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, true);
            }
        }
    }

    private ActivityDelegate fetchActivityDelegate(Activity activity) {
        ActivityDelegate activityDelegate = null;
        if (activity instanceof IActivity) {
            Cache<String, Object> cache = getCacheFromActivity((IActivity) activity);
            activityDelegate = (ActivityDelegate) cache.get(ActivityDelegate.ACTIVITY_DELEGATE_CACHE_KEY);
        }
        return activityDelegate;
    }

    @NonNull
    private Cache<String, Object> getCacheFromActivity(IActivity activity) {
        Cache<String, Object> cache = activity.provideCache();
        Preconditions.checkNotNull(activity, "%s cannot be null on Activity", Cache.class.getName());
        return cache;
    }
}