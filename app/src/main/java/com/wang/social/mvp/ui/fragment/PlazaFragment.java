package com.wang.social.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.frame.component.ui.base.BasicLazyNoDiFragment;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
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
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_FUNSHOW_LIST_TYPE_CHANGE: {
                //切换佬友筛选条件
                int type = (int) event.get("type");
                TextView textTab = tablayout.getTabAt(0).findViewById(R.id.custom_text);
                if (textTab != null) textTab.setText(type == 0 ? "趣晒" : "佬友");
                break;
            }
            case EventBean.EVENT_CHANGE_TAB_PLAZA_FUNSHOW: {
                //切换到趣晒
                if (pager != null) pager.setCurrentItem(0);
                break;
            }
        }
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
            if (position == 0 && pager.getCurrentItem() == 0) {
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
