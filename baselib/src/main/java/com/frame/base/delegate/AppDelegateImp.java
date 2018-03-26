package com.frame.base.delegate;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.frame.base.App;
import com.frame.di.component.AppComponent;
import com.frame.di.component.DaggerAppComponent;
import com.frame.di.module.GlobalConfigModule;
import com.frame.integration.ConfigModule;
import com.frame.utils.ManifestParser;
import com.frame.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * ========================================
 * {@link Application} 代理实现类，用于代理{@link Application}的生命周期，在对应的生命周期执行对应逻辑
 * <p>
 * Create by ChenJing on 2018-03-15 13:56
 * ========================================
 */

public class AppDelegateImp implements App, AppDelegate {

    private Application mApplication;
    private AppComponent mAppComponent;
    @Inject
    @Named("ActivityLifecycle")
    protected Application.ActivityLifecycleCallbacks mActivityLifecycle;
    @Inject
    @Named("ActivityLifecycleForRxLifecycle")
    protected Application.ActivityLifecycleCallbacks mActivityLifecycleForRxLifecycle;
    private List<ConfigModule> mModules;
    private List<AppDelegate> mAppDelegates = new ArrayList<>();
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();
    private ComponentCallbacks2 mComponentCallbacks;

    public AppDelegateImp(Context context) {
        //利用反射，将 AndroidManifest.xml 带有 ConfigModule 标签的class 转换成对象集合
        this.mModules = new ManifestParser(context).parse();
        //便利集合，执行每个ConfigModule 中的一些方法
        for (ConfigModule module : mModules) {
            //将框架外部实现的Application生命周期回掉存入mAppDelegates
            module.injectAppLifecycle(context, mAppDelegates);

            //将框架外部实现的Activity生命周期回掉存入mActivityLifecycles
            module.injectActivityLifecycle(context, mActivityLifecycles);
        }
    }

    @NonNull
    @Override
    public AppComponent getAppComponent() {
        Preconditions.checkNotNull(mAppComponent, "%s cannot be null,first call %s#onCreate(Application) in %s#onCreate()",
                AppComponent.class.getName(), getClass().getName(), Application.class.getName());
        return mAppComponent;
    }

    @Override
    public void attachBaseContext(Context base) {
        for (AppDelegate delegate : mAppDelegates) {
            delegate.attachBaseContext(base);
        }
    }

    @Override
    public void onCreate(Application application) {
        this.mApplication = application;

        mAppComponent = DaggerAppComponent
                .builder()
                .application(application)
                .globalConfigModule(getGlobalConfigModule(application, mModules))
                .build();

        mAppComponent.inject(this);

        this.mModules = null;

        //注册框架内部已实现的Activity生命周期逻辑
        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycle);

        //注册框架内部已实现的RxLifecycle 逻辑
        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycleForRxLifecycle);

        //注册框架外部扩展的 Activity 生命周期逻辑
        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
            mApplication.registerActivityLifecycleCallbacks(lifecycle);
        }

        mComponentCallbacks = new AppComponentCallbacks(mApplication, mAppComponent);

        //注册回调：内存紧张时释放内存
        mApplication.registerComponentCallbacks(mComponentCallbacks);

        //执行框架外部扩展的App onCreate逻辑
        for (AppDelegate delegate : mAppDelegates) {
            delegate.onCreate(mApplication);
        }
    }

    @Override
    public void onTerminate(Application application) {
        if (mActivityLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }
        if (mActivityLifecycleForRxLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycleForRxLifecycle);
        }
        if (mComponentCallbacks != null) {
            mApplication.unregisterComponentCallbacks(mComponentCallbacks);
        }
        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycleCallback : mActivityLifecycles) {
                mApplication.unregisterActivityLifecycleCallbacks(lifecycleCallback);
            }
        }
        if (mAppDelegates != null && mAppDelegates.size() > 0) {
            for (AppDelegate delegate : mAppDelegates) {
                delegate.onTerminate(mApplication);
            }
        }
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mActivityLifecycleForRxLifecycle = null;
        this.mActivityLifecycles = null;
        this.mComponentCallbacks = null;
        this.mAppDelegates = null;
        this.mApplication = null;
    }

    /**
     * 将App的全局配置信息封装进Module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest文件中声明{@link ConfigModule} 的实现类
     * @param application
     * @param modules
     * @return
     */
    private GlobalConfigModule getGlobalConfigModule(Application application, List<ConfigModule> modules) {
        GlobalConfigModule.Builder builder = GlobalConfigModule.builder();

        for (ConfigModule configModule : modules) {
            configModule.applyOptions(application, builder);
        }
        return builder.build();
    }

    private static class AppComponentCallbacks implements ComponentCallbacks2 {

        private Application mApplication;
        private AppComponent mAppComponent;

        public AppComponentCallbacks(Application mApplication, AppComponent mAppComponent) {
            this.mApplication = mApplication;
            this.mAppComponent = mAppComponent;
        }

        /**
         * 无论App生命周期的任何阶段，{@link ComponentCallbacks2#onTrimMemory(int)}发生的回调都预示着设备资源紧张
         * 应该根据{@link ComponentCallbacks2#onTrimMemory(int)}发生回掉时的内存级别来决定释放哪些资源
         *
         * @param level
         */
        @Override
        public void onTrimMemory(int level) {
            //状态1. 当开发者的 App 正在运行
            //设备开始运行缓慢, 不会被 kill, 也不会被列为可杀死的, 但是设备此时正运行于低内存状态下, 系统开始触发杀死 LRU 列表中的进程的机制
//                case TRIM_MEMORY_RUNNING_MODERATE:


            //设备运行更缓慢了, 不会被 kill, 但请你回收 unused 资源, 以便提升系统的性能, 你应该释放不用的资源用来提升系统性能 (但是这也会直接影响到你的 App 的性能)
//                case TRIM_MEMORY_RUNNING_LOW:


            //设备运行特别慢, 当前 App 还不会被杀死, 但是系统已经把 LRU 列表中的大多数进程都已经杀死, 因此你应该立即释放所有非必须的资源
            //如果系统不能回收到足够的 RAM 数量, 系统将会清除所有的 LRU 列表中的进程, 并且开始杀死那些之前被认为不应该杀死的进程, 例如那个包含了一个运行态 Service 的进程
//                case TRIM_MEMORY_RUNNING_CRITICAL:


            //状态2. 当前 App UI 不再可见, 这是一个回收大个资源的好时机
//                case TRIM_MEMORY_UI_HIDDEN:


            //状态3. 当前的 App 进程被置于 Background LRU 列表中
            //进程位于 LRU 列表的上端, 尽管你的 App 进程并不是处于被杀掉的高危险状态, 但系统可能已经开始杀掉 LRU 列表中的其他进程了
            //你应该释放那些容易恢复的资源, 以便于你的进程可以保留下来, 这样当用户回退到你的 App 的时候才能够迅速恢复
//                case TRIM_MEMORY_BACKGROUND:


            //系统正运行于低内存状态并且你的进程已经已经接近 LRU 列表的中部位置, 如果系统的内存开始变得更加紧张, 你的进程是有可能被杀死的
//                case TRIM_MEMORY_MODERATE:


            //系统正运行与低内存的状态并且你的进程正处于 LRU 列表中最容易被杀掉的位置, 你应该释放任何不影响你的 App 恢复状态的资源
            //低于 API 14 的 App 可以使用 onLowMemory 回调
//                case TRIM_MEMORY_COMPLETE:
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {

        }

        /**
         * 当系统开始清除LRU列表中的进程时，尽管会首先按照LRU的顺序来清除，但是他同时会考虑进程的内存使用量，因此消耗减少的进程容易被留下来
         */
        @Override
        public void onLowMemory() {
            //系统正运行与低内存的状态并且你的进程正处于 LRU 列表中最容易被杀掉的位置, 你应该释放任何不影响你的 App 恢复状态的资源
        }
    }
}
