package com.wang.social.login.mvp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.frame.base.BasicActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.wang.social.login.R;
import com.wang.social.login.di.component.DaggerResetPasswordComponent;
import com.wang.social.login.di.module.ResetPasswordModule;
import com.wang.social.login.mvp.contract.ResetPasswordContract;
import com.wang.social.login.utils.Keys;
import com.wang.social.login.utils.ViewUtils;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ResetPasswordActivity extends BasicActivity implements ResetPasswordContract.View {

    public static void start(Context context, String mobile, String verifyCode) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(Keys.NAME_MOBILE, mobile);
        intent.putExtra(Keys.NAME_VERIFY_CODE, verifyCode);
        context.startActivity(intent);
    }


    @BindView(R.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R.id.edit_text)
    EditText passwordEditText;
    @BindView(R.id.content_root)
    View contentRoot;
    @BindView(R.id.confirm_view)
    View confirmView;

    String mMobile;
    String mVerifyCode;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerResetPasswordComponent.builder()
                .appComponent(appComponent)
                .resetPasswordModule(new ResetPasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.login_activity_reset_password;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        // 上面传来的号码
        mMobile = getIntent().getStringExtra(Keys.NAME_MOBILE);
        // 验证码
        mVerifyCode = getIntent().getStringExtra(Keys.NAME_VERIFY_CODE);

        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (SocialToolbar.ClickType.LEFT_ICON == clickType) {
                    // 返回
                    ResetPasswordActivity.this.finish();
                }
            }
        });

        ViewUtils.controlKeyboardLayout(contentRoot, confirmView);
    }

    @OnCheckedChanged(R.id.checkbox)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            //如果选中，显示密码
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            //否则隐藏密码
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    @OnClick(R.id.confirm_view)
    public void confirm() {
        String password = passwordEditText.getText().toString();
        ViewUtils.hideSoftInputFromWindow(this, passwordEditText);

        Toast.makeText(this, "确定 : " + password, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastShort(msg);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
