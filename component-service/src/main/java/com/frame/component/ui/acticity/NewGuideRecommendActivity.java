package com.frame.component.ui.acticity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.adapter.PagerAdapterNewguideRecommend;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.utils.StatusBarUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;

public class NewGuideRecommendActivity extends BasicAppNoDiActivity {

    @BindView(R2.id.pager)
    ViewPager pager;

    private PagerAdapterNewguideRecommend pagerAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, NewGuideRecommendActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.activity_newguide_recommend;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        StatusBarUtil.setTranslucent(this);
        StatusBarUtil.setTextLight(this);
        pagerAdapter = new PagerAdapterNewguideRecommend(getSupportFragmentManager(), new String[]{"好友推荐", "趣聊推荐"});
        pager.setAdapter(pagerAdapter);
    }

}
