package com.wang.social.im.app;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import com.wang.social.im.utils.badge.ShortcutBadger;

/**
 * ============================================
 * <p>
 * <p>
 * Create by ChenJing on 2018-05-29 20:27
 * ============================================
 */
public class ImActivityLifecycleImpl implements Application.ActivityLifecycleCallbacks {

    //记录当前Activity是否在前台
    private int mLifeActivites = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (mLifeActivites == 0) {
            ShortcutBadger.applyCount(activity, 0);
            //清除通知
            ((NotificationManager) activity.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
        }
        mLifeActivites++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        mLifeActivites--;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
