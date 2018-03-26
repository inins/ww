package com.frame.base.delegate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.frame.di.component.AppComponent;
import com.frame.integration.cache.Cache;

/**
 * ======================================
 * 框架要求每个 {@link android.support.v4.app.Fragment} 都需实现此接口， 以满足规范
 * <p>
 * Create by ChenJing on 2018-03-12 15:49
 * ======================================
 */

public interface IFragment {

    /**
     * 提供{@link android.support.v4.app.Fragment} 中数据的缓存容器，
     * 此缓存容器跟{@link android.support.v4.app.Fragment} 生命周期绑定，{@link android.support.v4.app.Fragment} 销毁则缓存清空
     * @return
     */
    Cache<String, Object> provideCache();

    /**
     * 提供 AppComponent (提供所有的单例对象) 给实现类, 进行 Component 依赖
     * @param appComponent
     */
    void setupFragmentComponent(@NonNull AppComponent appComponent);

    /**
     * 是否使用 {@link org.simple.eventbus.EventBus}
     * @return
     */
    boolean useEventBus();

    /**
     * 初始化 View
     *
     * @param savedInstanceState
     * @return
     */
    int initView(@Nullable Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    void initData(@Nullable Bundle savedInstanceState);

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信，比如外部Activity想让自己持有的某个Fragment对象执行一些方法，
     * 建议在多个需要与外界交互的方法时，统一传{@link android.os.Message}, 通过what字段来区分不同的方法
     *
     * 调用此方法时请注意Fragment的生命周期， 如果调用{@link #setData(Object)}方法时{@link android.support.v4.app.Fragment#onCreate(Bundle)}还没执行
     * 但在{@link #setData(Object)}里却调用了presenter的方法，则会报空, 因为Dagger注入是在{@link android.support.v4.app.Fragment#onCreate(Bundle)}方法中执行
     * 然后才创建Presenter, 如果要做一些初始化操作, 可在{@link #initData(Bundle)}中初始化
     * @param data
     *
     */
    void setData(@Nullable Object data);
}