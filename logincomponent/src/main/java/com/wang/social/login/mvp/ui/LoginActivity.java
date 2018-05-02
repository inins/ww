package com.wang.social.login.mvp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.common.AppConstant;
import com.frame.component.helper.CommonHelper;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.acticity.tags.TagSelectionActivity;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.ToastUtil;
import com.umeng.socialize.UMShareAPI;
import com.wang.social.login.R;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.RouteNode;
import com.wang.social.login.R2;
import com.wang.social.login.di.component.DaggerLoginComponent;
import com.wang.social.login.di.module.LoginModule;
import com.wang.social.login.mvp.contract.LoginContract;
import com.wang.social.login.mvp.presenter.LoginPresenter;
import com.wang.social.login.mvp.ui.widget.CountDownView;
import com.wang.social.login.utils.StringUtils;
import com.wang.social.login.utils.ViewUtils;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

@RouteNode(path = "/login", desc = "登陆页")
public class LoginActivity extends BaseAppActivity<LoginPresenter> implements LoginContract.View {

    public final static String NAME_LAUNCH_MODE = "NAME_LAUNCH_MODE";
    public final static String LAUNCH_MODE_PASSWORD_LOGIN = "MODE_PASSWORD_LOGIN";
    public final static String LAUNCH_MODE_MESSAGE_LOGIN = "MODE_MESSAGE_LOGIN";
    public final static String LAUNCH_MODE_REGISTER = "MODE_REGISTER";

    public static void start(Context context, String launchMode) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(NAME_LAUNCH_MODE, launchMode);
        context.startActivity(intent);
    }

    @BindView(R2.id.root_view)
    View rootView;
    @BindView(R2.id.title_text_view)
    TextView titleTV; // 标题（登录/注册）
    @BindView(R2.id.phone_edit_text)
    EditText phoneET; // 手机号输入框
    @BindView(R2.id.password_login_layout)
    View passwordLoginLayout; // 密码登录输入View
    @BindView(R2.id.password_edit_text)
    EditText passwordET; // 密码输入框
    @BindView(R2.id.forgot_password_text_view)
    TextView forgotPasswordTV; // 忘记密码
    @BindView(R2.id.message_login_layout)
    View messageLoginLayout; // 短信登录输入View
    @BindView(R2.id.verify_code_edit_text)
    EditText verifyCodeET; // 验证码输入框
    @BindView(R2.id.get_verify_code_text_view)
    CountDownView getVerifyCodeTV; // 获取验证码
    @BindView(R2.id.switch_login_text_view)
    TextView switchLoginTV; // 切换登录模式
    @BindView(R2.id.third_party_login_layout)
    View thirdPartyLoginLayout;// 第三方登录按钮区域
    @BindView(R2.id.switch_login_register_text_view)
    TextView switchLoginRegisterTV; // 切换登录注册模式
    @BindView(R2.id.login_text_view)
    TextView loginTV; // 登录
    @BindView(R2.id.user_protocol_layout)
    View userProtocolLayout;


    String launchMode = LAUNCH_MODE_PASSWORD_LOGIN;


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
//        BarUtils.setTranslucent(this);
        StatusBarUtil.setTranslucent(this);
        StatusBarUtil.setTextDark(this);

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
            rootView.setBackground(getResources().getDrawable(R.drawable.login_bg));
        }

        resetLoginTVBg(false);

        // 监控输入框
        phoneET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                watchEditText();
            }
        });

        verifyCodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                watchEditText();
            }
        });

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                watchEditText();
            }
        });

        // 进入到登录页面则先清理缓存的Token
        mPresenter.clearToken();

//        ViewUtils.controlKeyboardLayout(rootView, loginTV);
    }

    private void watchEditText() {

        if (launchMode.equals(LAUNCH_MODE_PASSWORD_LOGIN)) {
            // 密码登录
            resetLoginTVBg(phoneET.getText().length() >= 11 && passwordET.getText().length() >= 6);
        } else if (launchMode.equals(LAUNCH_MODE_MESSAGE_LOGIN)) {
            // 短信登录
            resetLoginTVBg(phoneET.getText().length() >= 11 && verifyCodeET.getText().length() >= 4);
        } else {
            // 注册
            resetLoginTVBg(phoneET.getText().length() >= 11 && verifyCodeET.getText().length() >= 4 && passwordET.getText().length() >= 6);
        }
    }

    /**
     * 重设登录按钮背景色
     */
    private void resetLoginTVBg(boolean enable) {
        if (enable) {
            loginTV.setBackgroundResource(R.drawable.login_shape_rect_gradient_blue_corner);
        } else {
            loginTV.setBackgroundResource(R.drawable.login_shape_rect_gradient_disable_blue_corner);
        }

        loginTV.setEnabled(enable);
    }

    @OnClick(R2.id.switch_login_text_view)
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

    @OnClick(R2.id.switch_login_register_text_view)
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

    @OnClick(R2.id.forgot_password_text_view)
    public void forgotPassword() {
        ForgotPasswordActivity.start(this);
    }

    @OnCheckedChanged(R2.id.checkbox)
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

    @OnClick(R2.id.get_verify_code_text_view)
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

    @OnClick(R2.id.wechat_image_view)
    public void wechatLogin() {
        mPresenter.wxLogin();
    }

    @OnClick(R2.id.qq_image_view)
    public void qqLogin() {
        mPresenter.qqLogin();
    }

    @OnClick(R2.id.weibo_image_view)
    public void weiboLogin() {
        mPresenter.sinaLogin();
    }

    /**
     * 显示用户协议
     */
    @OnClick(R2.id.user_protocol_text_view)
    public void userProtocol() {
        WebActivity.start(this, AppConstant.Url.userAgreement);
    }

    /**
     * 登录
     */
    @OnClick(R2.id.login_text_view)
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
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }

    /**
     * 请求验证码成功后的回调
     */
    @Override
    public void onSendVerifyCodeSuccess() {
        // 验证码请求成功，开始倒计时
        getVerifyCodeTV.start();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    /**
     * 跳转到标签选择
     */
    @Override
    public void gotoTagSelection() {
        TagSelectionActivity.startSelectionFromLogin(this);
        finish();
    }

    /**
     * 跳转到首页
     */
    @Override
    public void gotoMainPage() {
        // 路由跳转
        CommonHelper.HomeHelper.startHomeActivity(this);
        finish();
    }


    /**
     * 友盟平台需要的回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




}
