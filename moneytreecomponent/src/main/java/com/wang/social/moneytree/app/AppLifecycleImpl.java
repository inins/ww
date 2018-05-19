package com.wang.social.moneytree.app;

import android.app.Application;
import android.content.Context;

import com.frame.base.delegate.AppDelegate;
import com.squareup.leakcanary.LeakCanary;
import com.wang.social.moneytree.BuildConfig;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 16:55
 * ========================================
 */

public class AppLifecycleImpl implements AppDelegate{
    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(Application application) {
    }

    @Override
    public void onTerminate(Application application) {

    }
}
