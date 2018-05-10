package com.wang.social.home.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.common.CardLayoutManager;
import com.wang.social.home.common.ItemTouchCardCallback;
import com.wang.social.home.mvp.ui.adapter.PagerAdapterCard;
import com.wang.social.home.mvp.ui.adapter.RecycleAdapterCard;
import com.wang.social.home.mvp.ui.dialog.CardPopupWindow;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardActivity extends BasicAppActivity {

    @BindView(R2.id.appbar)
    AppBarLayout appbar;
    @BindView(R2.id.tablayout)
    SmartTabLayout tablayout;
    @BindView(R2.id.pager)
    ViewPager pager;
    @BindView(R2.id.shadow)
    View shadow;

    private PagerAdapterCard pagerAdapter;

    private String[] titles = new String[]{"同类", "圈子"};

    private CardPopupWindow popupWindow;

    public static void start(Context context) {
        Intent intent = new Intent(context, CardActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.home_activity_card;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        appbar.bringToFront();

        pagerAdapter = new PagerAdapterCard(getSupportFragmentManager(), titles);
        pager.setAdapter(pagerAdapter);
        tablayout.setViewPager(pager);
        tablayout.setOnTabClickListener(position -> {
            if (position == 0) {
                popupWindow.showPopupWindow(tablayout.getTabAt(0));
            }
        });
        //第一个tab有一个下拉箭头
        CheckedTextView textTab1 = tablayout.getTabAt(0).findViewById(R.id.custom_text);
        textTab1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.home_select_down_up, 0);

        popupWindow = new CardPopupWindow(this);
        popupWindow.setShadowView(shadow);
        popupWindow.setCheckable(textTab1);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
    }
}
