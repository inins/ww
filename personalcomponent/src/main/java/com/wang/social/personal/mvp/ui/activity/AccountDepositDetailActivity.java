package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.ui.adapter.PagerAdapterAccountDepositDetail;
import com.wang.social.personal.mvp.ui.dialog.DepositePopupWindow;

import butterknife.BindView;

public class AccountDepositDetailActivity extends BasicAppNoDiActivity {

    @BindView(R2.id.tablayout)
    SmartTabLayout tablayout;
    @BindView(R2.id.pager)
    ViewPager pager;

    private PagerAdapterAccountDepositDetail pagerAdapter;
    private DepositePopupWindow popupWindowDiamond;
    private DepositePopupWindow popupWindowStone;

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
        popupWindowDiamond = new DepositePopupWindow(this);
        popupWindowStone = new DepositePopupWindow(this);
        popupWindowDiamond.setPosition(0);
        popupWindowStone.setPosition(1);
        pagerAdapter = new PagerAdapterAccountDepositDetail(getSupportFragmentManager(), new String[]{getString(R.string.common_diamond_name), getString(R.string.common_stone_name)});
        pager.setAdapter(pagerAdapter);
        tablayout.setViewPager(pager);
        tablayout.setOnTabClickListener(position -> {
            if (position == 0) {
                popupWindowDiamond.showPopupWindow(tablayout.getTabAt(position));
            } else if (position == 1) {
                popupWindowStone.showPopupWindow(tablayout.getTabAt(position));
            }
        });
        tablayout.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setDropImgByPosition(position);
            }
        });
    }

    private void setDropImgByPosition(int position) {
        TextView textTab1 = tablayout.getTabAt(0).findViewById(R.id.custom_text);
        TextView textTab2 = tablayout.getTabAt(1).findViewById(R.id.custom_text);
        if (textTab1 != null && textTab2 != null) {
            textTab1.setCompoundDrawablesWithIntrinsicBounds(0, 0, position == 0 ? R.drawable.common_ic_up : 0, 0);
            textTab2.setCompoundDrawablesWithIntrinsicBounds(0, 0, position == 1 ? R.drawable.common_ic_up : 0, 0);
        }
    }
}
