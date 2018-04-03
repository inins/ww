package com.wang.social.login.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.frame.base.BasicActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.wang.social.login.R;
import com.wang.social.login.utils.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgotPasswordActivity extends BasicActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, ForgotPasswordActivity.class);
        context.startActivity(intent);
    }


    @BindView(R.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R.id.edit_text)
    EditText phoneEditText;
    @BindView(R.id.content_root)
    View contentRoot;
    @BindView(R.id.get_verify_code)
    View getVerifyCodeView;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

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

    @OnClick(R.id.get_verify_code)
    public void getVerifyCode() {
        ViewUtils.hideSoftInputFromWindow(this, phoneEditText);

        String phone = phoneEditText.getText().toString();

        Toast.makeText(this,
                getString(R.string.login_get_verify_code) + " : " + phone,
                Toast.LENGTH_SHORT).show();
    }
}
