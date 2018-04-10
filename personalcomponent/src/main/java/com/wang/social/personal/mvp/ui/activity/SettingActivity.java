package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.frame.base.BaseActivity;
import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.ui.dialog.DialogBottomThirdLoginBind;

public class SettingActivity extends BasicActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_setting;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R2.id.btn_psw:
                break;
            case R2.id.btn_phone:
                break;
            case R2.id.btn_thirdlogin:
                new DialogBottomThirdLoginBind(this).show();
                break;
            case R2.id.btn_secret:
                PrivacyActivity.start(this);
                break;
            case R2.id.btn_clear:
                break;
            case R2.id.btn_shutdownlist:
                BlackListActivity.start(this);
                break;
            case R2.id.btn_blacklist:
                BlackListActivity.start(this);
                break;
            case R2.id.btn_msg:
                SettingMsgActivity.start(this);
                break;
            case R2.id.btn_logout:
                break;
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
