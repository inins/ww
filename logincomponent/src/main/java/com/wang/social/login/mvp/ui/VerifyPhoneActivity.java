package com.wang.social.login.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.frame.base.BasicActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.wang.social.login.R;
import com.wang.social.login.mvp.ui.widget.VerificationCodeInput;
import com.wang.social.login.utils.ViewUtils;

import butterknife.BindView;

public class VerifyPhoneActivity extends BasicActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, VerifyPhoneActivity.class);
        context.startActivity(intent);
    }


    @BindView(R.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R.id.content_root)
    View contentRoot;
    @BindView(R.id.phone_text_view)
    TextView phoneTextView;
    @BindView(R.id.send_again_text_view)
    TextView sendAgainTextView;
    @BindView(R.id.verification_code_input)
    VerificationCodeInput verificationCodeInput;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.login_activity_verify_phone;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (SocialToolbar.ClickType.LEFT_ICON == clickType) {
                    // 返回
                    VerifyPhoneActivity.this.finish();
                }
            }
        });

        phoneTextView.setText("13981920420");

        verificationCodeInput.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String content) {
                Toast.makeText(VerifyPhoneActivity.this, "验证码 : " +content, Toast.LENGTH_SHORT).show();
            }
        });

        ViewUtils.controlKeyboardLayout(contentRoot, sendAgainTextView);
    }
}
