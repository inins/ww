package com.wang.social.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.frame.base.BasicFragment;
import com.frame.di.component.AppComponent;
import com.wang.social.R;

/**
 * 建设中 fragment 占位
 */

public class BuildFragment extends BasicFragment {

    public static BuildFragment newInstance() {
        Bundle args = new Bundle();
        BuildFragment fragment = new BuildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.fragment_build;
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
