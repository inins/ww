package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.frame.base.BaseActivity;
import com.frame.base.BasicActivity;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.path.HomePath;
import com.frame.component.path.LoginPath;
import com.frame.component.router.ui.UIRouter;
import com.frame.di.component.AppComponent;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.ui.dialog.DialogBottomThirdLoginBind;
import com.wang.social.personal.mvp.ui.dialog.DialogSure;

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
        int i = v.getId();
        if (i == R.id.btn_psw) {
        } else if (i == R.id.btn_phone) {
        } else if (i == R.id.btn_thirdlogin) {
            new DialogBottomThirdLoginBind(this).show();
        } else if (i == R.id.btn_secret) {
            PrivacyActivity.start(this);
        } else if (i == R.id.btn_clear) {
        } else if (i == R.id.btn_shutdownlist) {
            BlackListActivity.start(this);
        } else if (i == R.id.btn_blacklist) {
            BlackListActivity.start(this);
        } else if (i == R.id.btn_msg) {
            SettingMsgActivity.start(this);
        } else if (i == R.id.btn_logout) {
            DialogSure.showDialog(this, "确定要退出登录？", () -> {
                AppDataHelper.removeToken();
                AppDataHelper.removeUser();
                UIRouter.getInstance().openUri(SettingActivity.this, LoginPath.LOGIN_URL, null);
                finish();
            });
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
