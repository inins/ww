package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.component.utils.viewutils.ViewPagerUtil;
import com.frame.utils.StatusBarUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.ui.adapter.PagerAdapterNewguideRecommend;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewGuideRecommendActivity extends BasicAppNoDiActivity {

    @BindView(R2.id.pager)
    ViewPager pager;
    @BindView(R2.id.img_banner)
    ImageView imgBanner;

    private PagerAdapterNewguideRecommend pagerAdapter;

    private String[] titles = new String[]{"好友推荐", "趣聊推荐"};
    private int[] picRscs = new int[]{R.drawable.bg_newguide_recommend_friend, R.drawable.bg_newguide_recommend_group};

    public static void start(Context context) {
        Intent intent = new Intent(context, NewGuideRecommendActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_newguide_recommend;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        StatusBarUtil.setTranslucent(this);
        StatusBarUtil.setTextLight(this);
        pagerAdapter = new PagerAdapterNewguideRecommend(getSupportFragmentManager(), new String[]{"好友推荐", "趣聊推荐"});
        pager.setAdapter(pagerAdapter);
    }

    public void changeBanner(int position) {
        imgBanner.setImageResource(picRscs[position]);
    }

    public void next(){
        ViewPagerUtil.next(pager);
    }
    public void last(){
        ViewPagerUtil.last(pager);
    }
}
