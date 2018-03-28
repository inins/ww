package com.wang.social.personal.app;

import android.app.Application;
import android.content.Context;

import com.frame.base.delegate.AppDelegate;
import com.wang.social.personal.BuildConfig;

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
        if (BuildConfig.LOG_DEBUG){
            Timber.plant(new Timber.DebugTree());

            ButterKnife.setDebug(true);
        }
    }

    @Override
    public void onTerminate(Application application) {

    }
}
