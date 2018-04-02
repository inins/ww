package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.personal.R;

public class SettingMsgActivity extends BasicActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingMsgActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_setting_msg;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {

    }

    public void onClick(View v){

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}