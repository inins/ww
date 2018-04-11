package com.wang.social.login.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.wang.social.login.R;
import com.wang.social.login.R2;
import com.wang.social.login.di.component.DaggerVerifyPhoneComponent;
import com.wang.social.login.di.module.VerifyPhoneModule;
import com.wang.social.login.mvp.contract.VerifyPhoneContract;
import com.wang.social.login.mvp.presenter.VerifyPhonePresenter;
import com.wang.social.login.mvp.ui.widget.VerificationCodeInput;
import com.wang.social.login.utils.Keys;
import com.wang.social.login.utils.ViewUtils;

import butterknife.BindView;
import timber.log.Timber;

public class VerifyPhoneActivity extends BaseAppActivity<VerifyPhonePresenter> implements
    VerifyPhoneContract.View{

    public static void start(Context context, String mobile) {
        Intent intent = new Intent(context, VerifyPhoneActivity.class);
        intent.putExtra(Keys.NAME_MOBILE, mobile);
        context.startActivity(intent);
    }


    @BindView(R2.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R2.id.content_root)
    View contentRoot;
    @BindView(R2.id.phone_text_view)
    TextView phoneTextView;
    @BindView(R2.id.send_again_text_view)
    TextView sendAgainTextView;
    @BindView(R2.id.verification_code_input)
    VerificationCodeInput verificationCodeInput;

    String mMobile;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerVerifyPhoneComponent.builder()
                .appComponent(appComponent)
                .verifyPhoneModule(new VerifyPhoneModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.login_activity_verify_phone;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        // 上面传来的号码
        mMobile = getIntent().getStringExtra(Keys.NAME_MOBILE);

        Timber.i("mobile = " + mMobile);

        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (SocialToolbar.ClickType.LEFT_ICON == clickType) {
                    // 返回
                    VerifyPhoneActivity.this.finish();
                }
            }
        });

        phoneTextView.setText(mMobile);

        verificationCodeInput.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String content) {
//                Toast.makeText(VerifyPhoneActivity.this, "验证码 : " +content, Toast.LENGTH_SHORT).show();
                // 验证验证码
                mPresenter.checkVerifyCode(mMobile, content);
            }
        });

        ViewUtils.controlKeyboardLayout(contentRoot, sendAgainTextView);
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }

    @Override
    public void onCheckVerifyCodeSuccess(String mobile, String code) {
        // 验证码验证成功，跳转到修改密码界面
        ResetPasswordActivity.start(this, mobile, code);

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
