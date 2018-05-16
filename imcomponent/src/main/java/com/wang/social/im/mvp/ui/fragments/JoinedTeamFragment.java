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
public class JoinedTeamFragment extends BasicFragment {

    public static JoinedTeamFragment newInstance() {

        Bundle args = new Bundle();

        JoinedTeamFragment fragment = new JoinedTeamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_fragment_joined_team;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }
}
