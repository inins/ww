package com.wang.social.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-06-05 17:20
 * ============================================
 */
public class ActivityLifecycleImpl implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        MobclickAgent.onResume(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        MobclickAgent.onPause(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
