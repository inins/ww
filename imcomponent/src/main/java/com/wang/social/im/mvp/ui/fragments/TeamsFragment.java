package com.wang.social.im.mvp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.frame.base.BasicFragment;
import com.frame.di.component.AppComponent;
import com.wang.social.im.R;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-15 16:07
 * ============================================
 */
public class TeamsFragment extends BasicFragment {

    public static TeamsFragment newInstance() {

        Bundle args = new Bundle();

        TeamsFragment fragment = new TeamsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_fragment_teams;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }
}
