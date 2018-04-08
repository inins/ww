package com.wang.social.login.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.base.BaseActivity;
import com.frame.utils.BarUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.login.R;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.RouteNode;
import com.wang.social.login.di.component.DaggerLoginComponent;
import com.wang.social.login.di.module.LoginModule;
import com.wang.social.login.mvp.contract.LoginContract;
import com.wang.social.login.mvp.presenter.LoginPresenter;
import com.wang.social.login.mvp.ui.widget.CountDownView;
import com.wang.social.login.mvp.ui.widget.DialogFragmentLoading;
import com.wang.social.login.utils.StringUtils;
import com.wang.social.login.utils.ViewUtils;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

@RouteNode(path = "/login", desc = "登陆页")
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    public final static String NAME_LAUNCH_MODE = "NAME_LAUNCH_MODE";
    public final static String LAUNCH_MODE_PASSWORD_LOGIN = "MODE_PASSWORD_LOGIN";
    public final static String LAUNCH_MODE_MESSAGE_LOGIN = "MODE_MESSAGE_LOGIN";
    public final static String LAUNCH_MODE_REGISTER = "MODE_REGISTER";

    public static void start(Context context, String launchMode) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(NAME_LAUNCH_MODE, launchMode);
        context.startActivity(intent);
    }

    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.title_text_view)
    TextView titleTV; // 标题（登录/注册）
    @BindView(R.id.phone_edit_text)
    EditText phoneET; // 手机号输入框
    @BindView(R.id.password_login_layout)
    View passwordLoginLayout; // 密码登录输入View
    @BindView(R.id.password_edit_text)
    EditText passwordET; // 密码输入框
    @BindView(R.id.forgot_password_text_view)
    TextView forgotPasswordTV; // 忘记密码
    @BindView(R.id.message_login_layout)
    View messageLoginLayout; // 短信登录输入View
    @BindView(R.id.verify_code_edit_text)
    EditText verifyCodeET; // 验证码输入框
    @BindView(R.id.get_verify_code_text_view)
    CountDownView getVerifyCodeTV; // 获取验证码
    @BindView(R.id.switch_login_text_view)
    TextView switchLoginTV; // 切换登录模式
    @BindView(R.id.third_party_login_layout)
    View thirdPartyLoginLayout;// 第三方登录按钮区域
    @BindView(R.id.switch_login_register_text_view)
    TextView switchLoginRegisterTV; // 切换登录注册模式
    @BindView(R.id.login_text_view)
    TextView loginTV; // 登录
    @BindView(R.id.user_protocol_layout)
    View userProtocolLayout;


    String launchMode = LAUNCH_MODE_PASSWORD_LOGIN;

    private DialogFragmentLoading mDialogLoading;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.login_activity_login_register;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        BarUtils.setTranslucent(this);

        if (this.getIntent().hasExtra(NAME_LAUNCH_MODE)) {
            launchMode = this.getIntent().getStringExtra(NAME_LAUNCH_MODE);
        }

        if (launchMode.equals(LAUNCH_MODE_PASSWORD_LOGIN)) {
            // 密码登录
            passwordLoginLayout.setVisibility(View.VISIBLE);
            messageLoginLayout.setVisibility(View.GONE);
            switchLoginTV.setVisibility(View.VISIBLE);
            thirdPartyLoginLayout.setVisibility(View.VISIBLE);
            forgotPasswordTV.setVisibility(View.VISIBLE);
            userProtocolLayout.setVisibility(View.GONE);
            switchLoginTV.setText(getString(R.string.login_message_login));
            titleTV.setText(getString(R.string.login_login));
            loginTV.setText(getString(R.string.login_login));
            passwordET.setHint(R.string.login_password_input_hint);
            switchLoginRegisterTV.setText(getString(R.string.login_go_to_register));
        } else if (launchMode.equals(LAUNCH_MODE_MESSAGE_LOGIN)) {
            // 短信登录
            passwordLoginLayout.setVisibility(View.GONE);
            messageLoginLayout.setVisibility(View.VISIBLE);
            switchLoginTV.setVisibility(View.VISIBLE);
            thirdPartyLoginLayout.setVisibility(View.VISIBLE);
            forgotPasswordTV.setVisibility(View.INVISIBLE);
            userProtocolLayout.setVisibility(View.GONE);
            switchLoginTV.setText(getString(R.string.login_password_login));
            titleTV.setText(getString(R.string.login_login));
            switchLoginRegisterTV.setText(getString(R.string.login_go_to_register));
        } else {
            // 注册
            passwordLoginLayout.setVisibility(View.VISIBLE);
            messageLoginLayout.setVisibility(View.VISIBLE);
            switchLoginTV.setVisibility(View.INVISIBLE);
            thirdPartyLoginLayout.setVisibility(View.GONE);
            forgotPasswordTV.setVisibility(View.GONE);
            userProtocolLayout.setVisibility(View.VISIBLE);
            titleTV.setText(getString(R.string.login_register));
            loginTV.setText(getString(R.string.login_register));
            passwordET.setHint(R.string.login_password_set_hint);
            switchLoginRegisterTV.setText(getString(R.string.login_go_to_login));
            rootView.setBackground(getResources().getDrawable(R.drawable.bg));
        }

        ViewUtils.controlKeyboardLayout(rootView, loginTV);
    }

    @OnClick(R.id.switch_login_text_view)
    public void switchLoginMode() {
        // 切换登录模式
        if (launchMode.equals(LAUNCH_MODE_MESSAGE_LOGIN)) {
            // 切换到密码登录
            start(this, LAUNCH_MODE_PASSWORD_LOGIN);
        } else if (launchMode.equals(LAUNCH_MODE_PASSWORD_LOGIN)) {
            // 切换到短信登录
            start(this, LAUNCH_MODE_MESSAGE_LOGIN);
        } else {
        }

        this.finish();
    }

    @OnClick(R.id.switch_login_register_text_view)
    public void switchLoginRegister() {
        // 切换登录注册模式
        if (launchMode.equals(LAUNCH_MODE_MESSAGE_LOGIN)) {
            // 切换到注册
            start(this, LAUNCH_MODE_REGISTER);
        } else if (launchMode.equals(LAUNCH_MODE_PASSWORD_LOGIN)) {
            // 切换到注册
            start(this, LAUNCH_MODE_REGISTER);
        } else {
            start(this, LAUNCH_MODE_PASSWORD_LOGIN);
        }

        this.finish();
    }

    @OnClick(R.id.forgot_password_text_view)
    public void forgotPassword() {
        ForgotPasswordActivity.start(this);
    }

    @OnCheckedChanged(R.id.checkbox)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            //如果选中，显示密码
            passwordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            //否则隐藏密码
            passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        // 将光标移至文字末尾
        passwordET.setSelection(passwordET.getText().length());
    }

    @OnClick(R.id.get_verify_code_text_view)
    public void setGetVerifyCode() {
        // 检测手机号
        if (!checkInputPhoneNO()) {
            showInputPhoneNOIllegal();

            return;
        }

        // 隐藏软键盘
        ViewUtils.hideSoftInputFromWindow(this, loginTV);

        // 用途类型
        //（注册 type=1;
        // 找回密码 type=2;
        // 三方账号绑定手机 type=4;
        // 更换手机号 type=5;
        // 短信登录 type=6）
        int type = 6;

        switch(launchMode) {
            case LAUNCH_MODE_PASSWORD_LOGIN:
                break;
            case LAUNCH_MODE_MESSAGE_LOGIN:
                type = 6;
                break;
            case LAUNCH_MODE_REGISTER:
                type = 1;
                break;
        }
        // 请求验证码
        mPresenter.sendVerifyCode(phoneET.getText().toString(), type);
    }

    @OnClick(R.id.wechat_image_view)
    public void wechatLogin() {

    }

    @OnClick(R.id.qq_image_view)
    public void qqLogin() {

    }

    @OnClick(R.id.weibo_image_view)
    public void weiboLogin() {

    }

    /**
     * 显示用户协议
     */
    @OnClick(R.id.user_protocol_text_view)
    public void userProtocol() {
        UserProtocolActivity.start(this);
    }

    /**
     * 登录
     */
    @OnClick(R.id.login_text_view)
    public void login() {
        switch(launchMode) {
            case LAUNCH_MODE_PASSWORD_LOGIN:
                passwordLogin();
                break;
            case LAUNCH_MODE_MESSAGE_LOGIN:
                messageLogin();
                break;
            case LAUNCH_MODE_REGISTER:
                register();
                break;
        }

        // 隐藏软键盘
        ViewUtils.hideSoftInputFromWindow(this, loginTV);
    }

    /**
     * 密码登录
     */
    private void passwordLogin() {
        // 检测手机号
        if (!checkInputPhoneNO()) {
            showInputPhoneNOIllegal();

            return;
        }

        // 检测密码
        if (!checkInputPassword()) {
            showInputPasswordIllegal();

            return;
        }

        // 密码登录
        mPresenter.passwordLogin(phoneET.getText().toString(),
                passwordET.getText().toString());
    }

    /**
     * 短信登录
     */
    private void messageLogin() {
        // 检测手机号
        if (!checkInputPhoneNO()) {
            showInputPhoneNOIllegal();

            return;
        }

        // 检测验证码
        if (!checkInputVerifyCode()) {
            showInputVerifyCodeIllegal();

            return;
        }

        // 短信登录
        mPresenter.messageLogin(phoneET.getText().toString(),
                verifyCodeET.getText().toString());
    }

    /**
     * 注册
     */
    private void register() {
        // 检测手机号
        if (!checkInputPhoneNO()) {
            showInputPhoneNOIllegal();

            return;
        }

        // 检测验证码
        if (!checkInputVerifyCode()) {
            showInputVerifyCodeIllegal();

            return;
        }

        // 检测密码
        if (!checkInputPassword()) {
            showInputPasswordIllegal();

            return;
        }

        // 注册
        mPresenter.register(phoneET.getText().toString(),
                verifyCodeET.getText().toString(),
                passwordET.getText().toString());
    }

    private boolean checkInputVerifyCode() {
        return StringUtils.isVerifyCode(verifyCodeET.getText().toString());
    }

    private void showInputVerifyCodeIllegal() {
        ToastUtil.showToastShort(getString(R.string.login_verify_code_input_illegal));
    }

    private boolean checkInputPhoneNO() {
        return StringUtils.isMobileNO(phoneET.getText().toString());
    }

    private void showInputPhoneNOIllegal() {
        ToastUtil.showToastShort(getString(R.string.login_phone_input_illegal));
    }

    private boolean checkInputPassword() {
        return StringUtils.isPassword(passwordET.getText().toString());
    }

    private void showInputPasswordIllegal() {
        ToastUtil.showToastShort(getString(R.string.login_password_input_illegal));
    }

    @Override
    public void showLoading() {
        mDialogLoading = DialogFragmentLoading.showDialog(getSupportFragmentManager(), TAG);
    }

    @Override
    public void hideLoading() {
        mDialogLoading.dismiss();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }

    @Override
    public void onSendVerifyCodeSuccess() {
        // 验证码请求成功，开始倒计时
        getVerifyCodeTV.start();
    }
}
