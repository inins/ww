package com.wang.social.login.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.base.BaseActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.wang.social.login.R;
import com.wang.social.login.R2;
import com.wang.social.login.di.component.DaggerResetPasswordComponent;
import com.wang.social.login.di.module.ResetPasswordModule;
import com.wang.social.login.mvp.contract.ResetPasswordContract;
import com.wang.social.login.mvp.presenter.ResetPasswordPresenter;
import com.wang.social.login.mvp.ui.widget.DialogFragmentLoading;
import com.wang.social.login.utils.Keys;
import com.wang.social.login.utils.StringUtils;
import com.wang.social.login.utils.ViewUtils;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import timber.log.Timber;

public class ResetPasswordActivity extends BaseActivity<ResetPasswordPresenter> implements ResetPasswordContract.View {

    public static void start(Context context, String mobile, String verifyCode) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(Keys.NAME_MOBILE, mobile);
        intent.putExtra(Keys.NAME_VERIFY_CODE, verifyCode);
        context.startActivity(intent);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        context.startActivity(intent);
    }


    @BindView(R2.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R2.id.edit_text)
    EditText passwordEditText;
    @BindView(R2.id.content_root)
    View contentRoot;
    @BindView(R2.id.confirm_view)
    View confirmView;
    @BindView(R2.id.title_text_view)
    TextView titleTV;
    @BindView(R2.id.title_hint_text_view)
    TextView titleHintTV;

    String mMobile;
    String mVerifyCode;
    boolean isResetPassword;

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
        if (getIntent().hasExtra(Keys.NAME_MOBILE) &&
                getIntent().hasExtra(Keys.NAME_VERIFY_CODE)) {
            // 上面传来的号码
            mMobile = getIntent().getStringExtra(Keys.NAME_MOBILE);
            // 验证码
            mVerifyCode = getIntent().getStringExtra(Keys.NAME_VERIFY_CODE);

            Timber.i("mobile = " + mMobile + " code = " + mVerifyCode);

            // 这是重置密码
            isResetPassword = true;
        }

        if (!isResetPassword) {
            titleTV.setText(R.string.login_set_password);
            titleHintTV.setVisibility(View.VISIBLE);
            titleHintTV.setText(R.string.login_set_password_hint);
        }

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

    @OnCheckedChanged(R2.id.checkbox)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            //如果选中，显示密码
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            //否则隐藏密码
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        // 将光标移至文字末尾
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    @OnClick(R2.id.confirm_view)
    public void confirm() {
        String password = passwordEditText.getText().toString();

        // 检测密码规则
        if (!StringUtils.isPassword(password)) {
            ToastUtil.showToastShort(getString(R.string.login_password_input_illegal));

            return;
        }

        ViewUtils.hideSoftInputFromWindow(this, passwordEditText);

        if (isResetPassword) {
            // 重设密码
            mPresenter.resetPassword(mMobile, mVerifyCode, password);
        } else {
            mPresenter.setPassword(password);
        }
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
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
