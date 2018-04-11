package com.wang.social.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.frame.base.BasicActivity;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.UnScrollViewPager;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.StatusBarUtil;
import com.wang.social.R;
import com.wang.social.mvp.ui.adapter.PagerAdapterHome;

import butterknife.BindView;

@RouteNode(path = "/main", desc = "首页")
public class MainActivity extends BasicAppActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.group_tab)
    RadioGroup groupTab;
    @BindView(R.id.pager)
    ViewPager pager;

    private PagerAdapterHome pagerAdapter;
    private int[] tabsId = new int[]{R.id.tab_1, R.id.tab_2, R.id.tab_3, R.id.tab_4};

    @Override
    public boolean useFragment() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarColor(this, R.color.common_blue_deep);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        groupTab.setOnCheckedChangeListener(this);
        pagerAdapter = new PagerAdapterHome(getSupportFragmentManager());
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(pagerAdapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        for (int i = 0; i < tabsId.length; i++) {
            if (tabsId[i] == checkedId) {
                pager.setCurrentItem(i, false);
            }
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
