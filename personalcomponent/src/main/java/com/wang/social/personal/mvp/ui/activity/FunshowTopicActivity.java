package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CheckedTextView;

import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.mvp.IView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.ui.adapter.PagerAdapterFunshowTopic;

import butterknife.BindView;

public class FunshowTopicActivity extends BasicAppActivity implements IView{

    @BindView(R2.id.appbar)
    AppBarLayout appbar;
    @BindView(R2.id.tablayout)
    SmartTabLayout tablayout;
    @BindView(R2.id.pager)
    ViewPager pager;

    private PagerAdapterFunshowTopic pagerAdapter;
    private String[] titles = new String[]{"趣晒", "话题"};
    private int position;

    public static void startFunshow(Context context) {
        start(context, 0);
    }

    public static void startTopic(Context context) {
        start(context, 1);
    }

    private static void start(Context context, int position) {
        Intent intent = new Intent(context, FunshowTopicActivity.class);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_funshow_topic;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        position = getIntent().getIntExtra("position", 0);
        appbar.bringToFront();

        pagerAdapter = new PagerAdapterFunshowTopic(getSupportFragmentManager(), titles);
        pager.setAdapter(pagerAdapter);
        tablayout.setViewPager(pager);

        pager.setCurrentItem(position, false);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }
}
