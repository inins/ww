package com.frame.base.delegate;

import android.app.Application;
import android.content.Context;

/**
 * =========================================
 * 用于代理{@link android.app.Application}的生命周期
 * {@link AppDelegate}可以代理{@link android.app.Application}的生命周期,执行对应的逻辑，因为Java只能单继承
 * 所以当App需要继承第三方的 Application类时，就可以不用再继承 BaseApplication 类，只需要在自定义的Application对应生命周期中调用AppDelegate方法
 * <p>
 * Create by ChenJing on 2018-03-15 13:52
 * =========================================
 */

public interface AppDelegate {

    void attachBaseContext(Context base);

    void onCreate(Application application);

    void onTerminate(Application application);
}
