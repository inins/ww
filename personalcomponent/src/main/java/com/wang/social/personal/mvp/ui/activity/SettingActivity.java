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
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.http.imageloader.ImageLoader;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.di.component.DaggerSingleActivityComponent;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.ui.dialog.DialogBottomThirdLoginBind;
import com.wang.social.personal.mvp.ui.dialog.DialogSure;
import com.wang.social.personal.net.helper.NetThirdLoginBindHelper;
import com.wang.social.socialize.SocializeUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import javax.inject.Inject;

import retrofit2.http.Field;

public class SettingActivity extends BasicAppActivity implements IView {

    @Inject
    NetThirdLoginBindHelper netThirdLoginBindHelper;

    private DialogBottomThirdLoginBind dialogThirdLogin;

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
        dialogThirdLogin = new DialogBottomThirdLoginBind(this);
        dialogThirdLogin.setOnThirdLoginDialogListener(new DialogBottomThirdLoginBind.OnThirdLoginDialogListener() {
            @Override
            public void onWeiXinClick(View v) {
                netThirdLoginBindHelper.netBindWeiXin(SettingActivity.this, SettingActivity.this);
            }

            @Override
            public void onWeiBoClick(View v) {
                netThirdLoginBindHelper.netBindWeiBo(SettingActivity.this, SettingActivity.this);
            }

            @Override
            public void onQQClick(View v) {
                netThirdLoginBindHelper.netBindQQ(SettingActivity.this, SettingActivity.this);
            }
        });
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_psw) {
            CommonHelper.LoginHelper.startSetPswActivity(this);
        } else if (i == R.id.btn_phone) {
            CommonHelper.LoginHelper.startBindPhoneActivity(this);
        } else if (i == R.id.btn_thirdlogin) {
            dialogThirdLogin.show();
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
                CommonHelper.LoginHelper.startLoginActivity(SettingActivity.this);
                EventBus.getDefault().post(new EventBean(EventBean.EVENT_LOGOUT));
            });
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSingleActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }
}
