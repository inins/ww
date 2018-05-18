package com.frame.component.ui.acticity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.ui.fragment.MeFunshowFragment;
import com.frame.component.ui.fragment.MeTopicFragment;
import com.frame.di.component.AppComponent;
import com.frame.mvp.IView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;

/**
 * groupId 为空时，加载自己的话题趣晒，如果存在groupId 则加载群成员的话题趣晒
 */
public class FunshowTopicActivity extends BasicAppActivity implements IView {

    @BindView(R2.id.appbar)
    AppBarLayout appbar;
    @BindView(R2.id.tablayout)
    SmartTabLayout tablayout;
    @BindView(R2.id.pager)
    ViewPager pager;

    private PagerAdapterFunshowTopic pagerAdapter;
    private String[] titles = new String[]{"趣晒", "话题"};
    private int position;
    private int groupId;
    private boolean isGroup;

    public static void startFunshow(Context context) {
        start(context, null, 0);
    }

    public static void startTopic(Context context) {
        start(context, null, 1);
    }

    public static void startFunshowForGroup(Context context, int groupId) {
        start(context, groupId, 0);
    }

    public static void startTopicForGroup(Context context, int groupId) {
        start(context, groupId, 1);
    }

    private static void start(Context context, Integer groupId, int position) {
        Intent intent = new Intent(context, FunshowTopicActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.activity_funshow_topic;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        position = getIntent().getIntExtra("position", 0);
        groupId = getIntent().getIntExtra("groupId", 0);
        isGroup = groupId != 0;
        appbar.bringToFront();

        pagerAdapter = new PagerAdapterFunshowTopic(getSupportFragmentManager());
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

    private class PagerAdapterFunshowTopic extends FragmentPagerAdapter {

        public PagerAdapterFunshowTopic(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (isGroup) {
                        return MeFunshowFragment.newInstanceForGroup(groupId);
                    } else {
                        return MeFunshowFragment.newInstance();
                    }
                case 1:
                    if (isGroup) {
                        return MeTopicFragment.newInstanceForGroup(groupId);
                    } else {
                        return MeTopicFragment.newInstance();
                    }
                default:
                    return null;
            }
        }
    }
}
