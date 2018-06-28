package com.wang.social.login202.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.frame.utils.StatusBarUtil;
import com.wang.social.login202.R;

/**
 * Created by King on 2018/4/2.
 */

public class MainActivity extends BasicActivity {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.login202_activity_main;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        StatusBarUtil.setTranslucent(this);
    }
}
