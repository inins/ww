package com.wang.social.login.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.base.BaseActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.wang.social.login.R;
import com.wang.social.login.di.component.DaggerBindPhoneComponent;
import com.wang.social.login.di.module.BindPhoneModule;
import com.wang.social.login.mvp.contract.BindPhoneContract;
import com.wang.social.login.mvp.presenter.BindPhonePresenter;
import com.wang.social.login.mvp.ui.widget.CountDownView;
import com.wang.social.login.mvp.ui.widget.DialogFragmentLoading;
import com.wang.social.login.utils.StringUtils;
import com.wang.social.login.utils.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class BindPhoneActivity extends BaseActivity<BindPhonePresenter> implements
    BindPhoneContract.View {

    public static void start(Context context) {
        Intent intent = new Intent(context, BindPhoneActivity.class);
        context.startActivity(intent);
    }


    @BindView(R.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R.id.phone_edit_text)
    EditText phoneEditText;
    @BindView(R.id.verify_code_edit_text)
    EditText verifyCodeET;
    @BindView(R.id.content_root)
    View contentRoot;
    @BindView(R.id.bind_text_view)
    TextView bindTV;
    @BindView(R.id.get_verify_code_text_view)
    CountDownView getVerifyCodeTV;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBindPhoneComponent.builder()
                .appComponent(appComponent)
                .bindPhoneModule(new BindPhoneModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.login_activity_bind_phone;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (SocialToolbar.ClickType.LEFT_ICON == clickType) {
                    // 返回
                    BindPhoneActivity.this.finish();
                }
            }
        });

        ViewUtils.controlKeyboardLayout(contentRoot, bindTV);
    }

    @OnClick(R.id.get_verify_code_text_view)
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
            mPresenter.sendVerifyCode(mobile, 5);
        } else {
            showToast(getString(R.string.login_phone_input_illegal));
        }
    }

    @OnClick(R.id.bind_text_view)
    public void vindPhone() {
        String mobile = phoneEditText.getText().toString();

        if (!StringUtils.isMobileNO(mobile)) {
            showToast(getString(R.string.login_phone_input_illegal));
        }

        String verifyCode = verifyCodeET.getText().toString();

        if (!StringUtils.isVerifyCode(verifyCode)) {
            showToast(getString(R.string.login_verify_code_input_illegal));
        }

        mPresenter.replaceMobile(mobile, verifyCode);
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }

    @Override
    public void onSendVerifyCodeSuccess(String mobile) {
        // 验证码请求成功，开始倒计时
        getVerifyCodeTV.start();
    }

    private DialogFragmentLoading mLoadingDialog;
    @Override
    public void showLoading() {
        mLoadingDialog = DialogFragmentLoading.showDialog(getSupportFragmentManager(), TAG);
    }

    @Override
    public void hideLoading() {
        mLoadingDialog.dismiss();
    }
}
