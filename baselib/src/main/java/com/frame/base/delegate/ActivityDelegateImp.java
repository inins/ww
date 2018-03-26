package com.frame.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.frame.utils.FrameUtils;

import org.simple.eventbus.EventBus;

/**
 * ========================================
 * {@link ActivityDelegate 实现类}
 * <p>
 * Create by ChenJing on 2018-03-13 11:57
 * ========================================
 */

public class ActivityDelegateImp implements ActivityDelegate {

    private Activity mActivity;
    private IActivity iActivity;

    public ActivityDelegateImp(Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceSate) {
        if (iActivity.useEventBus()){
            EventBus.getDefault().register(mActivity);
        }
        iActivity.setupActivityComponent(FrameUtils.obtainAppComponentFromContext(mActivity));
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroy() {
        if (iActivity != null && iActivity.useEventBus()){
            EventBus.getDefault().unregister(mActivity);
        }
        iActivity = null;
        mActivity = null;
    }
}
