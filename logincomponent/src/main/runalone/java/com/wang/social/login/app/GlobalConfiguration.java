package com.wang.social.login.app;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.frame.component.helper.AppDataHelper;
import com.wang.social.login.BuildConfig;
import com.frame.base.delegate.AppDelegate;
import com.frame.component.app.GlobalHttpHandlerImp;
import com.frame.component.app.ResponseErrorListenerImp;
import com.frame.di.module.GlobalConfigModule;
import com.frame.http.log.RequestInterceptor;
import com.frame.integration.ConfigModule;
import com.wang.social.socialize.SocializeUtil;

import java.util.List;

/**
 * ========================================
 * App 的全局配置信息
 * 组件化开发阶段，若一个组件独运行则需将此类复制到该组件下，并在Manifest文件中声明
 * ConfigModule 的实现类可以有无限个，若一个组件需要在回调中处理自己的逻辑， 则需照样实现ConfigModule，并在Manifest文件中声明
 * <p>
 * Create by ChenJing on 2018-03-20 9:20
 * ========================================
 */

public class GlobalConfiguration implements ConfigModule{

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        if (!BuildConfig.LOG_DEBUG){ //release的时候不再输出网络请求日志
            builder.printHttpLogLevel(RequestInterceptor.Level.NONE);
        }
        builder.baseUrl(BuildConfig.APP_DOMAIN)
                .globalHttpHandler(new GlobalHttpHandlerImp(context))
                .responseErrorListener(new ResponseErrorListenerImp());

//        AppDataHelper.saveToken("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzgyMzE1MDQyMCIsImNyZWF0ZWQiOjE1MjQ1NDIzMTc0MTksImlkIjoxMDAyNywiZXhwIjoxNTI1MTQ3MTE3fQ.ZrcLDZTaKekPvcZE_pfxFcAtLTAx6eCULrqoFGXezDZB7sQiFAISZQ2j_zX-DS2vZfnl4MNsU2cDEQvk0Wllmw");

    }

    @Override
    public void injectAppLifecycle(Context context, List<AppDelegate> lifecycles) {
        lifecycles.add(new AppLifecycleImpl());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycleCallbacks) {

    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycleCallbacks) {

    }
}
