package com.wang.social.login202.mvp.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.wang.social.login202.R;

public class Login202CardView extends FrameLayout {

    public interface CardViewCallback {
        /**
         * 手机号码登录
         * @param phone 手机号码
         */
        void onPhoneLogin(String phone);

        /**
         * 验证码登录获取验证码
         */
        void onLoginSendVerifyCode(CountDownView view);

        /**
         * 注册，获取验证码
         */
        void onRegisterSendVerifyCode(CountDownView view);

        /**
         * 注册，验证验证码
         * @param code 验证码
         */
        void onRegisterCheckVerifyCode(String code);

        /**
         * 邀请码 密码注册
         * @param password 密码
         * @param invitationCode 邀请码
         */
        void onRegisterLogin(String password, String invitationCode);

        /**
         * 切换到密码登录
         */
        void onSwitchToPasswordLogin();

        /**
         * 切换到验证码登录
         */
        void onSwitchToVerifyCodeLogin();

        /**
         * 密码登录
         * @param password 密码
         */
        void onPasswordLogin(String password);

        /**
         * 验证码登录
         * @param code 验证码
         */
        void onVerifyCodeLogin(String code);

        /**
         * 第三方登录，绑定手机号码
         * @param phone 需要绑定的手机号码
         */
        void onPlatformBindPhone(String phone);

        /**
         * 绑定手机，获取验证码
         */
        void onPlatformBindPhoneSendVerifyCode(CountDownView view);

        /**
         * 绑定手机 验证 登录
         * @param verifyCode 验证码
         * @param inviteCode 邀请码
         */
        void onPlatformBindPhoneVerify(String verifyCode, String inviteCode);
    }

    private View mRootView;
    private CardView mContentLayout;
    // 登录页面，输入手机号码
    private View mPhoneInputView;
    // 注册输入验证码
    private View mVerifyCodeInputView;
    // 注册，设置密码
    private View mRegisterSetPasswordView;
    // 密码登录
    private View mPasswordLoginView;
    // 验证码登录
    private View mVerifyCodeLoginView;
    // 绑定手机号码
    private View mBindPhoneView;
    // 绑定手机号码验证
    private View mBindPhoneVerifyView;

    private CardViewCallback mCallback;

    public Login202CardView(@NonNull Context context) {
        this(context, null);
    }

    public Login202CardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Login202CardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    public void setCallback(CardViewCallback callback) {
        mCallback = callback;
    }

    private void init(Context context) {
        mRootView = LayoutInflater.from(context)
                .inflate(R.layout.login202_widget_card_view, this, true);

        mContentLayout = mRootView.findViewById(R.id.card_view);

        loadLoginPhoneInputView();
//        loadVerifyCodeLoginView();
    }

    private void clearContentView() {
        if (null != mContentLayout) {
            mContentLayout.removeAllViews();
        }
    }

    /**
     * 登录 手机号码输入页面
     */
    public void loadLoginPhoneInputView() {
        clearContentView();

        if (null == mPhoneInputView) {
            mPhoneInputView = LayoutInflater.from(getContext())
                    .inflate(R.layout.login202_cv_phone_input, null);

            // 手机号码输入框
            EditText phoneET = mPhoneInputView.findViewById(R.id.phone_number_edit_text);

            // 下一步按钮
            mPhoneInputView.findViewById(R.id.next_btn)
                    .setOnClickListener(v -> {
                        if (null != mCallback) {
                            mCallback.onPhoneLogin(phoneET.getText().toString());
                        }
                    });
        }

        mContentLayout.addView(mPhoneInputView);
    }

    /**
     * 第三方登录 绑定手机号码页面
     */
    public void loadBindPhoneView() {
        clearContentView();

        if (null == mBindPhoneView) {
            mBindPhoneView = LayoutInflater.from(getContext())
                    .inflate(R.layout.login202_cv_bind_phone, null);

            // 手机号码输入框
            EditText phoneET = mBindPhoneView.findViewById(R.id.phone_number_edit_text);

            // 获取验证码
            mBindPhoneView.findViewById(R.id.button)
                    .setOnClickListener(v -> {
                        if (null != mCallback) {
                            mCallback.onPlatformBindPhone(phoneET.getText().toString());
                        }
                    });
        }

        mContentLayout.addView(mBindPhoneView);
    }

    /**
     * 第三方登录 绑定手机号码验证页面
     */
    public void loadBindPhoneVerifyView() {
        clearContentView();

        if (null == mBindPhoneVerifyView) {
            mBindPhoneVerifyView = LayoutInflater.from(getContext())
                    .inflate(R.layout.login202_cv_bind_phone_verify, null);

            // 获取验证码
            CountDownView countDownView = mBindPhoneVerifyView.findViewById(R.id.send_verify_code_text_view);
            countDownView.start();
            countDownView.setOnClickListener(v -> {
                if (null != mCallback) {
                    mCallback.onPlatformBindPhoneSendVerifyCode((CountDownView) v);
                }
            });

            // 验证码输入框
            EditText verifyCodeET = mBindPhoneVerifyView.findViewById(R.id.verify_code_edit_text);
            // 邀请码输入框
            EditText inviteCodeET = mBindPhoneVerifyView.findViewById(R.id.invite_code_edit_text);

            // 登录 注册
            mBindPhoneVerifyView.findViewById(R.id.button)
                    .setOnClickListener(v -> {
                        if (null != mCallback) {
                            mCallback.onPlatformBindPhoneVerify(
                                    verifyCodeET.getText().toString(),
                                    inviteCodeET.getText().toString()
                            );
                        }
                    });
        }

        mContentLayout.addView(mBindPhoneVerifyView);
    }



    /**
     * 注册，输入验证码
     */
    public void loadRegisterVerifyCodeInputView() {
        clearContentView();

        if (null == mVerifyCodeInputView) {
            mVerifyCodeInputView = LayoutInflater.from(getContext())
                    .inflate(R.layout.login202_cv_verify_code_input, null);

            // 获取验证码
            CountDownView countDownView = mVerifyCodeInputView.findViewById(R.id.send_verify_code_text_view);
            countDownView.start();
            countDownView.setOnClickListener(v -> {
                        if (null != mCallback) {
                            mCallback.onRegisterSendVerifyCode((CountDownView) v);
                        }
                    });

            // 验证码输入框
            EditText editText = mVerifyCodeInputView.findViewById(R.id.verify_code_edit_text);

            // 下一步，验证验证码
            mVerifyCodeInputView.findViewById(R.id.next_btn)
                    .setOnClickListener(v -> {
                        if (null != mCallback) {
                            mCallback.onRegisterCheckVerifyCode(editText.getText().toString());
                        }
                    });
        }

        mContentLayout.addView(mVerifyCodeInputView);
    }

    /**
     * 注册，设置密码
     */
    public void loadRegisterSetPasswordView() {
        clearContentView();

        if (null == mRegisterSetPasswordView) {
            mRegisterSetPasswordView = LayoutInflater.from(getContext())
                    .inflate(R.layout.login202_cv_set_password, null);

            // 密码输入框
            EditText passwordET = mRegisterSetPasswordView.findViewById(R.id.password_edit_text);
            // 密码是否可见
            CheckBox checkBox = mRegisterSetPasswordView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
                if (null != passwordET) {
                    if (isChecked) {
                        //如果选中，显示密码
                        passwordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        //否则隐藏密码
                        passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    // 将光标移至文字末尾
                    passwordET.setSelection(passwordET.getText().length());
                }
            });
            // 邀请码输入框
            EditText inviteCodeET = mRegisterSetPasswordView.findViewById(R.id.invite_code_edit_text);

            // 登录
            mRegisterSetPasswordView.findViewById(R.id.button)
                    .setOnClickListener(v -> {
                        if (null != mCallback) {
                            mCallback.onRegisterLogin(passwordET.getText().toString(), inviteCodeET.getText().toString());
                        }
                    });
        }

        mContentLayout.addView(mRegisterSetPasswordView);
    }

    /**
     * 密码登录
     */
    public void loadPasswordLoginView() {
        clearContentView();

        if (null == mPasswordLoginView) {
            mPasswordLoginView = LayoutInflater.from(getContext())
                    .inflate(R.layout.login202_cv_password_login, null);

            // 密码输入框
            EditText editText = mPasswordLoginView.findViewById(R.id.password_edit_text);
            // 密码是否可见
            CheckBox checkBox = mPasswordLoginView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
                if (null != editText) {
                    if (isChecked) {
                        //如果选中，显示密码
                        editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        //否则隐藏密码
                        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    // 将光标移至文字末尾
                    editText.setSelection(editText.getText().length());
                }
            });

            // 密码登录
            mPasswordLoginView.findViewById(R.id.button)
                    .setOnClickListener(v -> {
                        if (null != mCallback) {
                            mCallback.onPasswordLogin(editText.getText().toString());
                        }
                    });

            // 切换到验证码登录
            mPasswordLoginView.findViewById(R.id.verify_code_login_text_view)
                    .setOnClickListener(v -> {
                        if (null != mCallback) {
                            mCallback.onSwitchToVerifyCodeLogin();
                        }
                    });
        }

        mContentLayout.addView(mPasswordLoginView);
    }

    /**
     * 验证码登录
     */
    public void loadVerifyCodeLoginView() {
        clearContentView();

        if (null == mVerifyCodeLoginView) {
            mVerifyCodeLoginView = LayoutInflater.from(getContext())
                    .inflate(R.layout.login202_cv_verify_code_login, null);

            // 点击获取验证码
            mVerifyCodeLoginView.findViewById(R.id.send_verify_code_text_view)
                    .setOnClickListener(v -> {
                        if (null != mCallback && v instanceof CountDownView) {
                            mCallback.onLoginSendVerifyCode((CountDownView) v);
                        }
                    });

            // 切换到密码登录
            mVerifyCodeLoginView.findViewById(R.id.password_login_text_view)
                    .setOnClickListener(v -> {
                        if (null != mCallback) {
                            mCallback.onSwitchToPasswordLogin();
                        }
                    });

            // 验证码输入框
            EditText editText = mVerifyCodeLoginView.findViewById(R.id.verify_code_edit_text);

            // 验证码登录
            mVerifyCodeLoginView.findViewById(R.id.button)
                    .setOnClickListener(v -> {
                        if (null != mCallback && null != editText) {
                            mCallback.onVerifyCodeLogin(editText.getText().toString());
                        }
                    });
        }

        mContentLayout.addView(mVerifyCodeLoginView);
    }
}
