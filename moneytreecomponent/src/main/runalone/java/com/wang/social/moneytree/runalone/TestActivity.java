package com.wang.social.moneytree.runalone;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.frame.base.BasicActivity;
import com.frame.component.view.CircleProgress;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.wang.social.moneytree.R;
import com.wang.social.moneytree.R2;

import butterknife.BindView;

public class TestActivity extends BasicActivity {

    @BindView(R2.id.circle_progress)
    CircleProgress mCircleProgress;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.mt_activity_test;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mCircleProgress.setTextColor(getResources().getColor(R.color.common_blue_deep));
        mCircleProgress.setTextSize(SizeUtils.sp2px(36));
//        mCircleProgress.setStartColor(0xFF18E7FF);
//        mCircleProgress.setEndColor(0xFF189AFF);
        mCircleProgress.setProgress(90);
        mCircleProgress.setCircleWidth(20);
        mCircleProgress.invalidate();
    }
}
