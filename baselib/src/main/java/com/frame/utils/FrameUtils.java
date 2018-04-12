package com.frame.utils;

import android.content.Context;

import com.frame.base.App;
import com.frame.di.component.AppComponent;

/**
 * ======================================
 * 常用工具方法
 * <p>
 * Create by ChenJing on 2018-03-12 15:25
 * ======================================
 */

public class FrameUtils {

    private FrameUtils(){
        throw new IllegalStateException("Don't instantiate me!");
    }

    public static AppComponent  obtainAppComponentFromContext(Context context){
        Preconditions.checkNotNull(context, "%s cannot be null!", Context.class.getName());
        Preconditions.checkState(context.getApplicationContext() instanceof App, "Application does not implements App!");
        return ((App) context.getApplicationContext()).getAppComponent();
    }
}