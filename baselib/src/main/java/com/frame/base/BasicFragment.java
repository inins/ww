package com.frame.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.delegate.IFragment;
import com.frame.entities.EventBean;
import com.frame.integration.cache.Cache;
import com.frame.integration.cache.CacheType;
import com.frame.integration.lifecycle.FragmentLifecycleable;
import com.frame.mvp.BasePresenter;
import com.frame.utils.FrameUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * ======================================
 * <p>
 * <p>
 * Create by ChenJing on 2018-03-12 15:43
 * ======================================
 */

public abstract class BasicFragment extends Fragment implements IFragment, FragmentLifecycleable {

    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    private Cache<String, Object> mCache;

    protected Activity mActivity;
    protected View mRootView;
    private Unbinder mUnbinder;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(initView(savedInstanceState), container, false);
            mUnbinder = ButterKnife.bind(this, mRootView);
        } else {
            //避免重复添加View
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity = null;
        mRootView = null;
    }

    @Override
    public Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = FrameUtils.obtainAppComponentFromContext(mActivity).cacheFactory().build(CacheType.FRAGMENT_CACHE);
        }
        return mCache;
    }

    @NonNull
    @Override
    public Subject<FragmentEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    //默认的eventbus事件处理方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonEvent(EventBean event) {
    }

    /**
     * 用于Activity向Fragment传递返回点击事件
     *
     * @return 返回 false 表示不需拦截事件 返回 true 表示拦截
     */
    public boolean onBackPressed() {
        return false;
    }
}
