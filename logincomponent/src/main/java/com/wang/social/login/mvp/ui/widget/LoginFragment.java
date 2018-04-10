package com.wang.social.login.mvp.ui.widget;

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
import android.widget.Toast;

import com.frame.base.BaseFragment;
import com.frame.di.component.AppComponent;
import com.wang.social.login.R;
import com.wang.social.login.R2;
import com.wang.social.login.mvp.ui.LoginActivity;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment {
    public final static String NAME_LAUNCH_MODE = "NAME_LAUNCH_MODE";
    public final static String LAUNCH_MODE_PASSWORD_LOGIN = "MODE_PASSWORD_LOGIN";
    public final static String LAUNCH_MODE_MESSAGE_LOGIN = "MODE_MESSAGE_LOGIN";
    public final static String LAUNCH_MODE_REGISTER = "MODE_REGISTER";

    public static void start(Context context, String launchMode) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(NAME_LAUNCH_MODE, launchMode);
        context.startActivity(intent);
    }

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
    TextView getVerifyCodeTV; // 获取验证码
    @BindView(R2.id.switch_login_text_view)
    TextView switchLoginTV; // 切换登录模式
    @BindView(R2.id.third_party_login_layout)
    View thirdPartyLoginLayout;// 第三方登录按钮区域
    @BindView(R2.id.switch_login_register_text_view)
    TextView switchLoginRegisterTV; // 切换登录注册模式
    @BindView(R2.id.login_text_view)
    TextView loginTV; // 登录


    String launchMode = LAUNCH_MODE_MESSAGE_LOGIN;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.login_fragment_login;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (getActivity().getIntent().hasExtra(NAME_LAUNCH_MODE)) {
            launchMode = getActivity().getIntent().getStringExtra(NAME_LAUNCH_MODE);
        }

        if (launchMode.equals(LAUNCH_MODE_PASSWORD_LOGIN)) {
            // 密码登录
            passwordLoginLayout.setVisibility(View.VISIBLE);
            messageLoginLayout.setVisibility(View.GONE);
            switchLoginTV.setVisibility(View.VISIBLE);
            thirdPartyLoginLayout.setVisibility(View.VISIBLE);
            forgotPasswordTV.setVisibility(View.VISIBLE);
            switchLoginTV.setText(getString(R.string.login_message_login));
            titleTV.setText(getString(R.string.login_login));
            loginTV.setText(getString(R.string.login_login));
            switchLoginRegisterTV.setText(getString(R.string.login_go_to_register));
        } else if (launchMode.equals(LAUNCH_MODE_MESSAGE_LOGIN)) {
            // 短信登录
            passwordLoginLayout.setVisibility(View.GONE);
            messageLoginLayout.setVisibility(View.VISIBLE);
            switchLoginTV.setVisibility(View.VISIBLE);
            thirdPartyLoginLayout.setVisibility(View.VISIBLE);
            forgotPasswordTV.setVisibility(View.INVISIBLE);
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
            titleTV.setText(getString(R.string.login_register));
            loginTV.setText(getString(R.string.login_register));
            switchLoginRegisterTV.setText(getString(R.string.login_go_to_login));

        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @OnClick(R2.id.login_text_view)
    public void login() {

    }

    @OnClick(R2.id.switch_login_text_view)
    public void switchLoginMode() {
        // 切换登录模式
        if (launchMode.equals(LAUNCH_MODE_MESSAGE_LOGIN)) {
            // 切换到密码登录
            start(getActivity(), LAUNCH_MODE_PASSWORD_LOGIN);
        } else if (launchMode.equals(LAUNCH_MODE_PASSWORD_LOGIN)) {
            // 切换到短信登录
            start(getActivity(), LAUNCH_MODE_MESSAGE_LOGIN);
        } else {
        }

        getActivity().finish();
    }

    @OnClick(R2.id.switch_login_register_text_view)
    public void switchLoginRegister() {
        // 切换登录注册模式
        if (launchMode.equals(LAUNCH_MODE_MESSAGE_LOGIN)) {
            // 切换到注册
            start(getActivity(), LAUNCH_MODE_REGISTER);
        } else if (launchMode.equals(LAUNCH_MODE_PASSWORD_LOGIN)) {
            // 切换到注册
            start(getActivity(), LAUNCH_MODE_REGISTER);
        } else {
            start(getActivity(), LAUNCH_MODE_PASSWORD_LOGIN);
        }

        getActivity().finish();
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
    }

    @OnClick(R2.id.wechat_image_view)
    public void wechatLogin() {

    }

    @OnClick(R2.id.qq_image_view)
    public void qqLogin() {

    }

    @OnClick(R2.id.weibo_image_view)
    public void weiboLogin() {

    }
}
