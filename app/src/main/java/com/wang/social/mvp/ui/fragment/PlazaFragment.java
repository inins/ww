package com.wang.social.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.frame.component.ui.base.BasicLazyNoDiFragment;
import com.frame.di.component.AppComponent;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wang.social.R;
import com.wang.social.mvp.ui.adapter.PagerAdapterPlaza;
import com.wang.social.mvp.ui.dialog.FunshowSortPopupWindow;

import butterknife.BindView;

/**
 */

public class PlazaFragment extends BasicLazyNoDiFragment {

    @BindView(R.id.tablayout)
    SmartTabLayout tablayout;
    @BindView(R.id.pager)
    ViewPager pager;

    private FunshowSortPopupWindow popupWindow;
    private PagerAdapterPlaza pagerAdapter;

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
    public void initDataLazy() {
        popupWindow = new FunshowSortPopupWindow(getContext());
        pagerAdapter = new PagerAdapterPlaza(getChildFragmentManager(), titles);
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(1);
        tablayout.setViewPager(pager);
        tablayout.setOnTabClickListener(position -> {
            if (position == 0) {
                popupWindow.showPopupWindow(tablayout.getTabAt(0));
            }
        });
        //第一个tab有一个下拉箭头
        ((TextView) tablayout.getTabAt(0).findViewById(R.id.custom_text)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.common_ic_up, 0);
    }

    @Override
    public void setData(@Nullable Object data) {

    }


    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
    }
}
