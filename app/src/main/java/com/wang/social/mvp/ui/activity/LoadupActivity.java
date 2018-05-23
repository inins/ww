package com.wang.social.mvp.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.frame.component.helper.CommonHelper;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.R;

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
//                if (CommonHelper.LoginHelper.isLogin()) {
//                    HomeActivity.start(LoadupActivity.this);
//                } else {
//                    CommonHelper.LoginHelper.startLoginActivity(LoadupActivity.this);
//                }
                SplashActivity.start(LoadupActivity.this);
                finish();
            }
        }, 2000);
    }


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}

