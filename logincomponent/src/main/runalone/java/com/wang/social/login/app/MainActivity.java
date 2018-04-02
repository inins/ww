package com.wang.social.login.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.login.R;
import com.wang.social.login.mvp.ui.ForgotPasswordActivity;
import com.wang.social.login.mvp.ui.ResetPasswordActivity;
import com.wang.social.login.mvp.ui.VerifyPhoneActivity;

import butterknife.OnClick;

/**
 * Created by King on 2018/4/2.
 */

public class MainActivity extends BasicActivity {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.login_activity_main;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {

    }

    @OnClick(R.id.reset_password_btn)
    public void resetPassword() {
        ResetPasswordActivity.start(this);
    }

    @OnClick(R.id.forgot_password_btn)
    public void forgotPassword() {
        ForgotPasswordActivity.start(this);
    }

    @OnClick(R.id.verify_phone_btn)
    public void verifyPhone() {
        VerifyPhoneActivity.start(this);
    }
}
