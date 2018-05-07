package com.wang.social.home.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.frame.base.BasicFragment;
import com.frame.di.component.AppComponent;
import com.wang.social.home.R;

/**
 * 建设中 fragment 占位
 */

public class HomeFragment extends BasicFragment {

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.home_fragment;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void setData(@Nullable Object data) {

    }


    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
    }
}
