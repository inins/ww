package com.wang.social.personal.app;

import android.app.Application;
import android.content.Context;

import com.frame.base.delegate.AppDelegate;
import com.frame.utils.FrameUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.wang.social.personal.BuildConfig;
import com.wang.social.personal.data.db.AddressDataBaseManager;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 16:55
 * ========================================
 */

public class PersonalAppLifecycleImpl implements AppDelegate{
    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(Application application) {
        AddressDataBaseManager.init();
    }

    @Override
    public void onTerminate(Application application) {

    }
}
