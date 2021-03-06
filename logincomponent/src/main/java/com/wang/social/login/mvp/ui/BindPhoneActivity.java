package com.wang.social.login.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.squareup.leakcanary.RefWatcher;
import com.wang.social.login.R;
import com.wang.social.login.R2;
import com.wang.social.login.di.component.DaggerBindPhoneComponent;
import com.wang.social.login.di.module.BindPhoneModule;
import com.wang.social.login.mvp.contract.BindPhoneContract;
import com.wang.social.login.mvp.presenter.BindPhonePresenter;
import com.wang.social.login.mvp.ui.widget.CountDownView;
import com.frame.component.utils.StringUtils;
import com.wang.social.login.utils.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

@RouteNode(path = "/login_bind_phone", desc = "手机绑定")
public class BindPhoneActivity extends BaseAppActivity<BindPhonePresenter> implements
    BindPhoneContract.View {

    public static void start(Context context) {
        Intent intent = new Intent(context, BindPhoneActivity.class);
        context.startActivity(intent);
    }

    @BindView(R2.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R2.id.phone_edit_text)
    EditText phoneEditText;
    @BindView(R2.id.verify_code_edit_text)
    EditText verifyCodeET;
    @BindView(R2.id.content_root)
    View contentRoot;
    @BindView(R2.id.bind_text_view)
    TextView bindTV;
    @BindView(R2.id.get_verify_code_text_view)
    CountDownView getVerifyCodeTV;
    @BindView(R2.id.title_text_view)
    TextView titleTV;

    // 是否是换绑手机
    boolean isRebind;

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

        isRebind = userHasPhone();
        if (isRebind) {
            titleTV.setText(R.string.login_rebind_phone);
        }

        phoneEditText.addTextChangedListener(new TextWatcher() {
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

        resetBindTVBg(false);

//        ViewUtils.controlKeyboardLayout(contentRoot, bindTV);
    }

    private void watchEditText() {
        resetBindTVBg(phoneEditText.getText().length() >= 11 &&
                verifyCodeET.getText().length() >= 4);
    }

    /**
     * 重设按钮背景色
     */
    private void resetBindTVBg(boolean enable) {
        if (enable) {
            bindTV.setBackgroundResource(R.drawable.login_shape_rect_gradient_blue_corner);
        } else {
            bindTV.setBackgroundResource(R.drawable.login_shape_rect_gradient_disable_blue_corner);
        }
        bindTV.setEnabled(enable);
    }

    public boolean userHasPhone() {
        User user = AppDataHelper.getUser();

        if (null != user) {
            String phone = user.getPhone();
            if (StringUtils.isMobileNO(phone)) {
                return true;
            }
        }


        return false;
    }

    /**
     * 获取验证码
     */
    @OnClick(R2.id.get_verify_code_text_view)
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

    /**
     * 绑定按钮
     */
    @OnClick(R2.id.bind_text_view)
    public void bindPhone() {
        String mobile = phoneEditText.getText().toString();

        if (!StringUtils.isMobileNO(mobile)) {
            showToast(getString(R.string.login_phone_input_illegal));

            return;
        }

        String verifyCode = verifyCodeET.getText().toString();

        if (!StringUtils.isVerifyCode(verifyCode)) {
            showToast(getString(R.string.login_verify_code_input_illegal));

            return;
        }

        mPresenter.replaceMobile(mobile, verifyCode);
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }

    /**
     * 验证码申请成功
     */
    @Override
    public void onSendVerifyCodeSuccess(String mobile) {
        // 验证码请求成功，开始倒计时
        getVerifyCodeTV.start();
    }

    /**
     * 换绑手机成功，跳转到设置密码页面
     */
    @Override
    public void onReplaceMobileSuccess() {
        if (!isRebind) {
            ResetPasswordActivity.start(this);
        } else {
            showToast(getString(R.string.login_rebind_phone_success));
        }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (FrameUtils.obtainAppComponentFromContext(this).extras().get(RefWatcher.class.getName())
                instanceof RefWatcher) {
            Timber.i("Watch this!");
            RefWatcher refWatcher = (RefWatcher)FrameUtils.obtainAppComponentFromContext(this).extras().get(RefWatcher.class.getName());

            refWatcher.watch(this);
        }
    }
}
