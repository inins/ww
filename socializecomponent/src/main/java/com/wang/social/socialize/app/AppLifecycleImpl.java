package com.wang.social.socialize.app;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.frame.base.delegate.AppDelegate;
import com.wang.social.socialize.SocializeUtil;

public class AppLifecycleImpl implements AppDelegate{
    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        SocializeUtil.init(application);
    }

    @Override
    public void onTerminate(Application application) {

    }
}
