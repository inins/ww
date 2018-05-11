package com.wang.social.im.mvp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.frame.base.BasicFragment;
import com.frame.di.component.AppComponent;
import com.wang.social.im.R;

/**
 * ============================================
 * 通知
 * <p>
 * Create by ChenJing on 2018-05-07 19:56
 * ============================================
 */
public class NotifyFragment extends BasicFragment{

    public static NotifyFragment newInstance() {
        Bundle args = new Bundle();
        NotifyFragment fragment = new NotifyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_fragment_notify;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }
}
