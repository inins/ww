package com.wang.social.im.app;

import android.app.Application;
import android.content.Context;

import com.frame.base.App;
import com.frame.base.delegate.AppDelegate;
import com.frame.utils.FrameUtils;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;
import com.wang.social.im.BuildConfig;

/**
 * ======================================
 * {@link Application}代理实现类
 * <p>
 * Create by ChenJing on 2018-04-04 14:19
 * ======================================
 */
public class ImAppLifecycleImpl implements AppDelegate{

    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(Application application) {
        imSdkInit(application);
    }

    @Override
    public void onTerminate(Application application) {

    }

    /**
     * IM SDK初始化
     */
    private void imSdkInit(Application application) {
        //初始化SDK基本配置
        TIMSdkConfig config = new TIMSdkConfig(1)
                .enableCrashReport(false) //是否开启Crash上报
                .enableLogPrint(BuildConfig.LOG_DEBUG) //设置是否打印日志
                .setLogLevel(TIMLogLevel.DEBUG) //设置日志打印级别
                .setLogPath(FrameUtils.obtainAppComponentFromContext(application).cacheFile().getPath() + "/imlog/"); //设置日志保存路径
        //初始化
        TIMManager.getInstance().init(application, config);
    }
}
