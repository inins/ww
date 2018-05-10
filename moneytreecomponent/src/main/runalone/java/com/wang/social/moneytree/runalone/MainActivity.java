package com.wang.social.moneytree.runalone;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.moneytree.R;
import com.wang.social.moneytree.R2;
import com.wang.social.moneytree.mvp.ui.widget.CountDownTextView;

import butterknife.BindView;

public class MainActivity extends BasicActivity {

    @BindView(R2.id.count_down_text_view)
    CountDownTextView mCountDownTextView;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.mt_activity_main;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mCountDownTextView.start(120000);
    }
}
