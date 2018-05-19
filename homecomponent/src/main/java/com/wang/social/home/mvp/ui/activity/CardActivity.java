package com.wang.social.home.mvp.ui.activity;

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
import com.frame.utils.StatusBarUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.ui.adapter.PagerAdapterCard;
import com.wang.social.home.mvp.ui.dialog.CardPopupWindow;

import butterknife.BindView;

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
    private int position;

    public static void startUser(Context context) {
        start(context, 0);
    }

    public static void startGroup(Context context) {
        start(context, 1);
    }

    private static void start(Context context, int position) {
        Intent intent = new Intent(context, CardActivity.class);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.home_activity_card;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        position = getIntent().getIntExtra("position", 0);
        appbar.bringToFront();
        StatusBarUtil.setTranslucent(this);


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

        pager.setCurrentItem(position, false);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
    }
}
