package com.wang.social.mvp.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.utils.StatusBarUtil;
import com.wang.social.R;
import com.wang.social.mvp.ui.adapter.PagerAdapterHome;

import butterknife.BindView;

public class LoadupActivity extends BasicAppActivity {

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.activity_loadup;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.start(LoadupActivity.this);
                finish();
            }
        }, 2000);
    }


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}

