package com.wang.social.funshow.app;

import android.app.Application;
import android.content.Context;

import com.frame.base.delegate.AppDelegate;
import com.frame.component.path.FunshowPath;
import com.frame.component.router.ui.UIRouter;
import com.frame.utils.FrameUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.wang.social.funshow.BuildConfig;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 16:55
 * ========================================
 */

public class AppLifecycleImpl implements AppDelegate {
    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(Application application) {
        UIRouter.getInstance().registerUI(FunshowPath.HOST);
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        if (BuildConfig.LOG_DEBUG) {
            Timber.plant(new Timber.DebugTree());

            ButterKnife.setDebug(true);
        }
        //LeakCanary检查内存泄漏
        FrameUtils.obtainAppComponentFromContext(application).extras().put(RefWatcher.class.getName(), BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);
    }

    @Override
    public void onTerminate(Application application) {

    }
}
