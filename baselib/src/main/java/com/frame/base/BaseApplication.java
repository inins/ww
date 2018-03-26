package com.frame.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.frame.base.delegate.AppDelegate;
import com.frame.base.delegate.AppDelegateImp;
import com.frame.di.component.AppComponent;
import com.frame.utils.Preconditions;

/**
 * ========================================
 * 框架由 MVP + Dagger2 + Retrofit + RxJava + AndroidEventBus + ButterKnife 组成
 * <p>
 * Create by ChenJing on 2018-03-19 9:42
 * ========================================
 */

public class BaseApplication extends Application implements App {

    private AppDelegate mAppDelegate;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (mAppDelegate == null) {
            this.mAppDelegate = new AppDelegateImp(base);
        }
        this.mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mAppDelegate != null) {
            mAppDelegate.onCreate(this);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null) {
            this.mAppDelegate.onTerminate(this);
        }
    }

    @NonNull
    @Override
    public AppComponent getAppComponent() {
        Preconditions.checkNotNull(mAppDelegate, "%s cannot be null!", AppDelegateImp.class.getName());
        Preconditions.checkState(mAppDelegate instanceof App, "%s must be implements %s", AppDelegateImp.class.getName(), App.class.getName());
        return ((AppDelegateImp) mAppDelegate).getAppComponent();
    }
}
