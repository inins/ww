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
import com.frame.component.helper.CommonHelper;
import com.frame.component.path.HomePath;
import com.frame.component.path.LoginPath;
import com.frame.component.router.ui.UIRouter;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.ui.dialog.DialogBottomThirdLoginBind;
import com.wang.social.personal.mvp.ui.dialog.DialogSure;

import org.greenrobot.eventbus.EventBus;

public class SettingActivity extends BasicAppActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_LOGOUT:
                finish();
                break;
        }
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
            UIRouter.getInstance().openUri(this, LoginPath.LOGIN_RESET_PASSWORD_URL, null);
        } else if (i == R.id.btn_phone) {
            UIRouter.getInstance().openUri(this, LoginPath.LOGIN_BIND_PHONE_URL, null);
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
//                UIRouter.getInstance().openUri(SettingActivity.this, LoginPath.LOGIN_URL, null);
                CommonHelper.LoginHelper.startLoginActivity(SettingActivity.this);
                EventBus.getDefault().post(new EventBean(EventBean.EVENT_LOGOUT));
            });
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
