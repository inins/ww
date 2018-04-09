package com.frame.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.frame.base.delegate.IActivity;
import com.frame.component.service.AutowiredService;
import com.frame.integration.cache.Cache;
import com.frame.integration.cache.CacheType;
import com.frame.integration.lifecycle.ActivityLifecycleable;
import com.frame.mvp.BasePresenter;
import com.frame.utils.FrameUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * ======================================
 * Activity 基类
 * 因为Java只能单继承，所以如果要用到继承特定{@link android.app.Activity} 的三方库，那就需要自定义{@link android.app.Activity}
 * 继承于这个特定的{@link android.app.Activity}，然后再按照{@link BasicActivity} 的格式，将代码复制过去，一定要实现{@link IActivity}
 * <p>
 * Create by ChenJing on 2018-03-12 15:08
 * ======================================
 */

public abstract class BasicActivity extends AppCompatActivity implements IActivity, ActivityLifecycleable {

    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
    private Cache<String, Object> mCache;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            int layoutResId = initView(savedInstanceState);
            if (layoutResId != 0) {
                setContentView(layoutResId);

                mUnbinder = ButterKnife.bind(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        beforeInitData();
        initData(savedInstanceState);

        //将页面自动装载到组件框架,以供其他组件调用
        AutowiredService.Factory.getInstance().create().autowire(this);
    }

    //这个方法在initData前执行，用于在设置数据前进行一些必要的初始化
    protected void beforeInitData(){}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        this.mUnbinder = null;
    }

    @NonNull
    @Override
    public Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = FrameUtils.obtainAppComponentFromContext(this).cacheFactory().build(CacheType.ACTIVITY_CACHE);
        }
        return mCache;
    }

    @NonNull
    @Override
    public Subject<ActivityEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public boolean useFragment() {
        return true;
    }
}