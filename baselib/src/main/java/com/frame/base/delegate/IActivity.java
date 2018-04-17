package com.frame.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.frame.di.component.AppComponent;
import com.frame.integration.cache.Cache;

/**
 * ======================================
 * 框架要求每个{@link android.app.Activity} 都需实现此类，以满足规范
 * <p>
 * Create by ChenJing on 2018-03-12 15:09
 * ======================================
 */

public interface IActivity {

    /**
     * 提供在 {@link Activity} 中的缓存容器,可在此{@link Activity} 中存取一些必要的数据
     * 此缓存容器与{@link Activity}生命周期绑定，若{@link Activity}销毁则缓存也清空
     * @return
     */
    @NonNull
    Cache<String, Object> provideCache();

    /**
     * 提供 AppComponent (提供所有的单例对象)给实现类, 进行 Component 依赖
     * @param appComponent
     */
    void setupActivityComponent(@NonNull AppComponent appComponent);

    /**
     * 是否使用 {@link org.greenrobot.eventbus.EventBus}
     * @return
     */
    boolean useEventBus();

    /**
     * 初始化 View, 若 {@link #initView(Bundle)} 返回0， 则框架不会调用{@link android.app.Activity#setContentView(int)}
     *
     * @param savedInstanceState
     */
    int initView(@NonNull Bundle savedInstanceState);

    /**
     * 初始化数据
     * @param savedInstanceState
     */
    void initData(@NonNull Bundle savedInstanceState);

    /**
     * 这个 Activity 是否会使用 Fragment, 框架会根据这个属性判断是否注册{@link android.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回{@code false},那意味着这个Activity不需要绑定Fragment，那你再在这个Activity中绑定继承于{@link com.frame.base.BaseFragment} 的Fragment将不再起作用
     * @see com.frame.integration.ActivityLifecycle#registerFragmentCallbacks(Activity) (Fragment的注册过程)
     * @return
     */
    boolean useFragment();
}
