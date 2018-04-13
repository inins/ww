package com.wang.social.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.BasicFragment;
import com.frame.di.component.AppComponent;
import com.wang.social.R;
import com.wang.social.mvp.ui.adapter.PagerAdapterPlaza;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 建设中 fragment 占位
 */

public class PlazaFragment extends BasicFragment {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.pager)
    ViewPager pager;

    private PagerAdapterPlaza pagerAdapter;
    Unbinder unbinder;

    private String[] titles = new String[]{"趣晒", "话题", "趣点"};

    public static PlazaFragment newInstance() {
        Bundle args = new Bundle();
        PlazaFragment fragment = new PlazaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.fragment_plaza;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        pagerAdapter = new PagerAdapterPlaza(getChildFragmentManager(), titles);
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(3);
        tablayout.setupWithViewPager(pager);
    }

    @Override
    public void setData(@Nullable Object data) {

    }


    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
