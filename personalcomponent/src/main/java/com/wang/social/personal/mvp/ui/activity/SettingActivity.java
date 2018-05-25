package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.CommonHelper;
import com.frame.component.router.Router;
import com.frame.component.service.funshow.FunshowService;
import com.frame.component.service.im.ImService;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;
import com.umeng.socialize.UMShareAPI;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.di.component.DaggerSingleActivityComponent;
import com.wang.social.personal.mvp.ui.dialog.DialogBottomThirdLoginBind;
import com.frame.component.ui.dialog.DialogSure;
import com.wang.social.personal.net.helper.NetThirdLoginBindHelper;
import com.wang.social.personal.utils.ClearCacheUtil;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;

public class SettingActivity extends BasicAppActivity implements IView {

    @BindView(R2.id.text_clear)
    TextView text_clear;

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
        dialogThirdLogin.setOnThirdLoginDialogListener(onThirdLoginDialogListener);

        text_clear.setText(ClearCacheUtil.getAppCacheSize(this));
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_psw) {
            //修改密码前需要先绑定手机号
            if (!CommonHelper.LoginHelper.isLogin()) return;
            if (TextUtils.isEmpty(AppDataHelper.getUser().getPhone())) {
                DialogSure.showDialog(this, "请先绑定手机？", () -> {
                    CommonHelper.LoginHelper.startBindPhoneActivity(this);
                });
            } else {
                CommonHelper.LoginHelper.startSetPswActivity(this);
            }
        } else if (i == R.id.btn_phone) {
            CommonHelper.LoginHelper.startBindPhoneActivity(this);
        } else if (i == R.id.btn_thirdlogin) {
            //绑定三方账号前需要先绑定手机号
            if (!CommonHelper.LoginHelper.isLogin()) return;
            if (TextUtils.isEmpty(AppDataHelper.getUser().getPhone())) {
                DialogSure.showDialog(this, "请先绑定手机？", () -> {
                    CommonHelper.LoginHelper.startBindPhoneActivity(this);
                });
            } else {
                netGetBindHistoryAndShow();
            }
        } else if (i == R.id.btn_secret) {
            PrivacyActivity.start(this);
        } else if (i == R.id.btn_clear) {
            if (ClearCacheUtil.getAppCacheSizeValue(this) == 0) {
                ToastUtil.showToastShort("没有需要清除的缓存");
                return;
            }
            ClearCacheUtil.clearAPPCache(SettingActivity.this);
            text_clear.setText(ClearCacheUtil.getAppCacheSize(SettingActivity.this));
        } else if (i == R.id.btn_shutdownlist) {
            BlackListActivity.startShutdownList(this);
        } else if (i == R.id.btn_blacklist) {
            BlackListActivity.startBlankList(this);
        } else if (i == R.id.btn_msg) {
            SettingMsgActivity.start(this);
        } else if (i == R.id.btn_logout) {
            DialogSure.showDialog(this, "确定要退出登录？", () -> {
                //移除token和用户数据
                AppDataHelper.removeToken();
                AppDataHelper.removeUser();
                //退出Im登录
                ImService imService = (ImService) Router.getInstance().getService(ImService.class.getName());
                if (imService != null) imService.imLogout();
                //启动登录页面
                CommonHelper.LoginHelper.startLoginActivity(SettingActivity.this);
                //通知其他组件
                EventBus.getDefault().post(new EventBean(EventBean.EVENT_LOGOUT));
            });
        }
    }

    //绑定弹窗点击事件
    private DialogBottomThirdLoginBind.OnThirdLoginDialogListener onThirdLoginDialogListener = new DialogBottomThirdLoginBind.OnThirdLoginDialogListener() {
        @Override
        public void onWeiXinClick(View v, boolean binded) {
            if (!binded) {
                netThirdLoginBindHelper.netBindWeiXin(SettingActivity.this, SettingActivity.this);
            } else {
                DialogSure.showDialog(SettingActivity.this, "确定要解绑微信？", () -> {
                    netThirdLoginBindHelper.netUnBindWeiXin(SettingActivity.this);
                });
            }
        }

        @Override
        public void onWeiBoClick(View v, boolean binded) {
            if (!binded) {
                netThirdLoginBindHelper.netBindWeiBo(SettingActivity.this, SettingActivity.this);
            } else {
                DialogSure.showDialog(SettingActivity.this, "确定要解绑微博？", () -> {
                    netThirdLoginBindHelper.netUnBindWeiBo(SettingActivity.this);
                });
            }
        }

        @Override
        public void onQQClick(View v, boolean binded) {
            if (!binded) {
                netThirdLoginBindHelper.netBindQQ(SettingActivity.this, SettingActivity.this);
            } else {
                DialogSure.showDialog(SettingActivity.this, "确定要解绑QQ？", () -> {
                    netThirdLoginBindHelper.netUnBindQQ(SettingActivity.this);
                });
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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

    //检查绑定状态，并弹出窗口
    private void netGetBindHistoryAndShow() {
        netThirdLoginBindHelper.netBindList(this, (bindHistories) -> {
            dialogThirdLogin.setBindHistories(bindHistories);
            dialogThirdLogin.show();
        });
    }
}
