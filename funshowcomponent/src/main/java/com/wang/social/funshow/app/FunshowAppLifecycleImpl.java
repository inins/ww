package com.wang.social.funshow.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.frame.base.delegate.AppDelegate;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 16:55
 * ========================================
 */

public class FunshowAppLifecycleImpl implements AppDelegate {
    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(Application application) {
        Log.e("notice", "这里进行初始化工作");
    }

    @Override
    public void onTerminate(Application application) {

    }
}
