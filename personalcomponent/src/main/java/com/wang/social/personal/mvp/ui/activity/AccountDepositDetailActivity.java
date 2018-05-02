package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.utils.FocusUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.TestEntity;
import com.wang.social.personal.mvp.ui.adapter.PagerAdapterAccountDepositDetail;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterDepositDetail;
import com.wang.social.personal.mvp.ui.dialog.DepositePopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountDepositDetailActivity extends BasicAppActivity {

    @BindView(R2.id.tablayout)
    TabLayout tablayout;
    @BindView(R2.id.pager)
    ViewPager pager;

    private PagerAdapterAccountDepositDetail pagerAdapter;
    private DepositePopupWindow popupWindow;

    public static void start(Context context) {
        Intent intent = new Intent(context, AccountDepositDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_account_deposit_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        popupWindow = new DepositePopupWindow(this);
        pagerAdapter = new PagerAdapterAccountDepositDetail(getSupportFragmentManager(), new String[]{getString(R.string.common_diamond_name), getString(R.string.common_stone_name)});
        pager.setAdapter(pagerAdapter);
        tablayout.setupWithViewPager(pager);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
