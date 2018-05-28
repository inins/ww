package com.wang.social.login.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.integration.AppManager;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.ToastUtil;
import com.wang.social.login.R;
import com.wang.social.login.R2;
import com.wang.social.login.di.component.DaggerResetPasswordComponent;
import com.wang.social.login.di.module.ResetPasswordModule;
import com.wang.social.login.mvp.contract.ResetPasswordContract;
import com.wang.social.login.mvp.presenter.ResetPasswordPresenter;
import com.wang.social.login.utils.Keys;
import com.frame.component.utils.StringUtils;
import com.wang.social.login.utils.ViewUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

@RouteNode(path = "/login_reset_password", desc = "修改/重置密码")
public class ResetPasswordActivity extends BaseAppActivity<ResetPasswordPresenter> implements ResetPasswordContract.View {

    public static void start(Context context, String mobile, String verifyCode, @Keys.TypePasswordUI int type) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(Keys.NAME_MOBILE, mobile);
        intent.putExtra(Keys.NAME_VERIFY_CODE, verifyCode);
        intent.putExtra(Keys.NAME_PASSWOR_UI_TYPE, type);
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
    @Keys.TypePasswordUI int mUIType;

    @Inject
    AppManager appManager;

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
        // UI类型，默认设置密码
        mUIType = getIntent().getIntExtra(Keys.NAME_PASSWOR_UI_TYPE, Keys.TYPE_SET_PASSWORD);
        // 上面传来的号码
        mMobile = getIntent().getStringExtra(Keys.NAME_MOBILE);
        // 验证码
        mVerifyCode = getIntent().getStringExtra(Keys.NAME_VERIFY_CODE);

        // 设置密码
        if (mUIType == Keys.TYPE_SET_PASSWORD) {
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

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                resetConfirmTVBg(s.toString().length() >= 6);
            }
        });

        resetConfirmTVBg(false);

//        ViewUtils.controlKeyboardLayout(contentRoot, confirmView);
    }

    /**
     * 重设按钮背景色
     */
    private void resetConfirmTVBg(boolean enable) {
        if (enable) {
            confirmView.setBackgroundResource(R.drawable.login_shape_rect_gradient_blue_corner);
        } else {
            confirmView.setBackgroundResource(R.drawable.login_shape_rect_gradient_disable_blue_corner);
        }

        confirmView.setEnabled(enable);
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

        // 设置密码
        if (mUIType == Keys.TYPE_SET_PASSWORD) {
            mPresenter.setPassword(password);
        } else {
            // 重设密码
            mPresenter.resetPassword(mMobile, mVerifyCode, password);
        }
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }

    /**
     * 修改密码成功
     */
    @Override
    public void onResetPasswordSuccess() {
        ToastUtil.showToastLong(getString(R.string.login_reset_success));

        finish();

        appManager.killActivity(ForgotPasswordActivity.class);
        appManager.killActivity(VerifyPhoneActivity.class);
    }

    @Override
    public void onSetPasswordSuccess() {
        ToastUtil.showToastLong(getString(R.string.login_set_success));

        finish();
    }


    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }
}
