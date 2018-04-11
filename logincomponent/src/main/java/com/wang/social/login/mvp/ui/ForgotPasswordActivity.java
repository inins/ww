package com.wang.social.login.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.frame.base.BaseActivity;
import com.frame.base.BasicActivity;
import com.frame.component.router.Router;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.ToastUtil;
import com.wang.social.login.R;
import com.wang.social.login.R2;
import com.wang.social.login.di.component.DaggerForgotPasswordComponent;
import com.wang.social.login.di.module.ForgotPasswordModule;
import com.wang.social.login.mvp.contract.ForgotPasswordContract;
import com.wang.social.login.mvp.presenter.ForgotPasswordPresenter;
import com.wang.social.login.mvp.ui.widget.DialogFragmentLoading;
import com.wang.social.login.utils.StringUtils;
import com.wang.social.login.utils.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;

@RouteNode(path = "/forgotPassword", desc = "忘记密码")
public class ForgotPasswordActivity extends BaseAppActivity<ForgotPasswordPresenter> implements
    ForgotPasswordContract.View {

    public static void start(Context context) {
        Intent intent = new Intent(context, ForgotPasswordActivity.class);
        context.startActivity(intent);
    }


    @BindView(R2.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R2.id.edit_text)
    EditText phoneEditText;
    @BindView(R2.id.content_root)
    View contentRoot;
    @BindView(R2.id.get_verify_code)
    View getVerifyCodeView;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerForgotPasswordComponent.builder()
                .appComponent(appComponent)
                .forgotPasswordModule(new ForgotPasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.login_activity_forgot_password;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (SocialToolbar.ClickType.LEFT_ICON == clickType) {
                    // 返回
                    ForgotPasswordActivity.this.finish();
                }
            }
        });

        ViewUtils.controlKeyboardLayout(contentRoot, getVerifyCodeView);
    }

    @OnClick(R2.id.get_verify_code)
    public void getVerifyCode() {
        ViewUtils.hideSoftInputFromWindow(this, phoneEditText);

        String mobile = phoneEditText.getText().toString();

        if (StringUtils.isMobileNO(mobile)) {
            /**
             * 用途类型
             （注册 type=1;
             找回密码 type=2;
             三方账号绑定手机 type=4;
             更换手机号 type=5;
             短信登录 type=6）
             */
            mPresenter.sendVerifyCode(mobile, 2);
        } else {
            showToast(getString(R.string.login_phone_input_illegal));
        }
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }

    @Override
    public void onSendVerifyCodeSuccess(String mobile) {
        // 获取验证码成功，跳转到输入验证码界面
        VerifyPhoneActivity.start(this, mobile);

//        finish();
    }

//    private DialogFragmentLoading mLoadingDialog;
    @Override
    public void showLoading() {
//        mLoadingDialog = DialogFragmentLoading.showDialog(getSupportFragmentManager(), TAG);
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
//        mLoadingDialog.dismiss();
        dismissLoadingDialog();
    }
}
