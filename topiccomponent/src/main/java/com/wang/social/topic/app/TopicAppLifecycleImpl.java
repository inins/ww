package com.wang.social.topic.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.frame.base.delegate.AppDelegate;
import com.frame.component.helper.AppDataHelper;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 16:55
 * ========================================
 */

public class TopicAppLifecycleImpl implements AppDelegate {
    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(Application application) {
        Log.e("notice", "这里进行初始化工作");

        AppDataHelper.saveToken("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzgyMzE1MDQyMCIsImNyZWF0ZWQiOjE1MjQyODI3MTE1MTEsImlkIjoxMDAyNywiZXhwIjoxNTI0ODg3NTExfQ.hiJzHYUjDK1OT2M1sRbgKa9Jicf-OJ3MtvWXGjMON8JH-uCDBHS6PSh54RXl3rOx-qHl7GGEpJ7H76pnYi3nLQ");
    }

    @Override
    public void onTerminate(Application application) {

    }
}
