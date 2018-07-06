package com.wang.social.login202.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.frame.component.common.AppConstant;
import com.frame.component.helper.CommonHelper;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.acticity.tags.TagSelectionActivity;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.dialog.DialogConfirm;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.RegexUtils;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.ToastUtil;
import com.umeng.socialize.UMShareAPI;
import com.wang.social.login202.R;
import com.wang.social.login202.R2;
import com.wang.social.login202.mvp.contract.Login202Contract;
import com.wang.social.login202.mvp.model.entities.CheckPhoneResult;
import com.wang.social.login202.mvp.model.entities.CheckVerifyCode;
import com.wang.social.login202.mvp.model.entities.LoginInfo;
import com.wang.social.login202.mvp.presenter.Login202Presenter;
import com.wang.social.login202.mvp.ui.adapter.CardAdapter;
import com.wang.social.login202.mvp.ui.widget.CountDownView;
import com.wang.social.login202.mvp.ui.widget.Login202CardView;
import com.wang.social.login202.mvp.ui.widget.Login202TitleView;
import com.wang.social.login202.mvp.ui.widget.infinitecards.AnimationTransformer;
import com.wang.social.login202.mvp.ui.widget.infinitecards.CardItem;
import com.wang.social.login202.mvp.ui.widget.infinitecards.InfiniteCardView;
import com.wang.social.login202.mvp.ui.widget.infinitecards.ZIndexTransformer;
import com.wang.social.login202.mvp.ui.widget.infinitecards.transformer.DefaultCommonTransformer;
import com.wang.social.login202.mvp.ui.widget.infinitecards.transformer.DefaultTransformerToBack;
import com.wang.social.login202.mvp.ui.widget.infinitecards.transformer.DefaultZIndexTransformerCommon;
import com.wang.social.login202.mvp.util.Constants;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

@RouteNode(path = "/login", desc = "登陆页")
public class Login202Activity extends BaseAppActivity implements Login202Contract.View, Login202CardView.CardViewCallback {

    // 返回按钮
    @BindView(R2.id.back_image_view)
    ImageView mBackIV;
    // 标题View
    @BindView(R2.id.title_view)
    Login202TitleView mTitleView;
    // 中间卡片
    @BindView(R2.id.infinite_card_view)
    InfiniteCardView mCardView;
    CardAdapter mCardAdapter;
    // 第三方登录区域
    @BindView(R2.id.third_part_login_layout)
    View mThirdPartLoginLayout;
    // 用户协议区域
    @BindView(R2.id.user_protocol_layout)
    View mUserProtocolLayout;

    // 文字 登录
    @BindString(R2.string.login202_login)
    String mStrLogin;
    // 文字 密码登录
    @BindString(R2.string.login202_password_login)
    String mStrPasswordLogin;
    // 文字 验证码登录
    @BindString(R2.string.login202_verify_code_login)
    String mStrVerifyCodeLogin;
    // 文字 手机号
    @BindString(R2.string.login202_phone_number)
    String mStrPhoneNum;
    // 文字 已将验证码发送至 xxx
    @BindString(R2.string.login202_verify_code_sent_hint)
    String mStrVerifyCodeSentFormat;
    // 文字 输入验证码
    @BindString(R2.string.login202_input_verify_code)
    String mStrInputVerifyCode;
    // 文字 设置密码
    @BindString(R2.string.login202_set_password)
    String mStrSetPassword;
    // 文字 设置密码提示
    @BindString(R2.string.login202_register_set_password_hint)
    String mStrSetPasswordHint;
    // 文字 用户取消
    @BindString(R2.string.login202_user_cancel)
    String mStrUserCancel;
    // 文字 绑定手机
    @BindString(R2.string.login202_bind_phone)
    String mStrBindPhone;
    // 文字 绑定手机提示
    @BindString(R2.string.login202_bind_phone_hint)
    String mStrBindPhoneHint;

    // 记录输入的手机号码
    private String mPhoneNumber;
    // 记录验证码
    private String mVerifyCode;

    // 卡片View
    private List<Login202CardView> mCardList = new ArrayList<>();

    // 记录状态，在动画完成显示对应的内容
    // 是否显示顶部返回按钮
    private boolean mShowBackIV;
    // 标题
    private String mTitle;
    // 标题提示
    private String mTitleHint;
    // 是否显示第三方登录
    private boolean mShowThirdPartLoginLayout;
    // 是否显示用户协议
    private boolean mShowUserProtocolLayout;

    private Login202Contract.Presenter mPresenter;
    // 记录当前点击的倒计时控件
    private CountDownView mCurrentCDV;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.login202_activity_login;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        StatusBarUtil.setTranslucent(this);

        mPresenter = new Login202Presenter(this);

        initCardViews();

        mCardAdapter = new CardAdapter(this, mCardList);
        mCardView.setAdapter(mCardAdapter);
        mCardView.setCardAnimationListener(new InfiniteCardView.CardAnimationListener() {
            @Override
            public void onAnimationStart() {
                // 动画开始，隐藏所有其他元素
                hideAllOtherView();
            }

            @Override
            public void onAnimationEnd() {
                // 动画结束，显示需要显示的元素
                showOtherView();
            }
        });

        // 默认显示 登录 页面
        mBackIV.setVisibility(View.GONE);
        mTitleView.setTitle(mStrLogin);
    }


    /**
     * 友盟平台需要的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void initCardViews() {
        for (int i = 0; i < 3; i++) {
            Login202CardView view = new Login202CardView(this);
            view.setCallback(this);
            mCardList.add(view);
        }
    }

    /**
     * 显示第三方登录区域
     *
     * @param visible 是否显示
     */
    private void showThirdPartLogin(boolean visible) {
        mThirdPartLoginLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 显示用户协议文字
     *
     * @param visible 是否显示
     */
    private void showUserProtocol(boolean visible) {
        mUserProtocolLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 重置其他元素的显示状态，在动画结束后按照这个设置来显示对应的元素
     *
     * @param showBackIV          是否显示顶部返回按钮
     * @param title               标题
     * @param titleHint           副标题
     * @param showThirdPartLayout 是否显示第三方登录
     * @param showUserProtocol    是否显示用户协议
     */
    private void resetOtherView(boolean showBackIV, String title, String titleHint, boolean showThirdPartLayout,
                                boolean showUserProtocol) {
        mShowBackIV = showBackIV;
        mTitle = title;
        mTitleHint = titleHint;
        mShowThirdPartLoginLayout = showThirdPartLayout;
        mShowUserProtocolLayout = showUserProtocol;
    }

    /**
     * 显示其他元素
     */
    private void showOtherView() {
        mBackIV.setVisibility(mShowBackIV ? View.VISIBLE : View.GONE);
        mTitleView.setVisibility(View.VISIBLE);
        mTitleView.setTitle(mTitle);
        mTitleView.setHint(mTitleHint);
        showThirdPartLogin(mShowThirdPartLoginLayout);
        showUserProtocol(mShowUserProtocolLayout);
    }

    /**
     * 动画开始是隐藏所有其他元素
     */
    private void hideAllOtherView() {
        mBackIV.setVisibility(View.GONE);
        mTitleView.setVisibility(View.GONE);
        showThirdPartLogin(false);
        showUserProtocol(false);
    }

    /**
     * 点击显示用户协议
     */
    @OnClick(R2.id.user_protocol_text_view)
    public void userProtocol() {
        WebActivity.start(this, AppConstant.Url.userAgreement);
    }

    /**
     * 微信登录
     */
    @OnClick(R2.id.wx_image_view)
    public void wxLogin() {
        Timber.i("微信登录");
        mPresenter.wxLogin();
    }

    /**
     * QQ登录
     */
    @OnClick(R2.id.qq_image_view)
    public void qqLogin() {
        Timber.i("QQ登录");
        mPresenter.qqLogin();
    }

    /**
     * 微博登录
     */
    @OnClick(R2.id.wb_image_view)
    public void wbLogin() {
        Timber.i("微博登录");
        mPresenter.sinaLogin();
    }

    /**
     * 返回按键处理
     */
    @Override
    public void onBackPressed() {
        if (mBackIV.getVisibility() == View.VISIBLE) {
            setSimulateClick(mBackIV, mBackIV.getX(), mBackIV.getY());
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 模拟点击
     *
     * @param view 需要点击的View
     * @param x    x
     * @param y    y
     */
    private void setSimulateClick(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

    /**
     * 返回 登录，手机号码输入页面
     */
    private void backToLoginPhoneInput() {
        if (null != getCardView(mCardAdapter.getCount() - 1)) {
            getCardView(mCardAdapter.getCount() - 1).loadLoginPhoneInputView();
            lastPage();

            resetOtherView(false, mStrLogin, "", true, true);
        }
    }

    /**
     * 下一页，手机号码登录
     */
    private void nextToPasswordLogin() {
        if (null != getCardView(1)) {
            getCardView(1).loadPasswordLoginView();
            // 切换到下一页
            nextPage();

            resetOtherView(true,
                    mStrPasswordLogin,
                    mStrPhoneNum + " " + mPhoneNumber,
                    false, false);

            // 点击返回按钮返回 登录 手机号码输入
            mBackIV.setOnClickListener(v -> backToLoginPhoneInput());
        }
    }

    /**
     * 上一页，手机号码登录
     */
    private void backToPasswordLogin() {
        if (null != getCardView(mCardAdapter.getCount() - 1)) {
            getCardView(mCardAdapter.getCount() - 1).loadPasswordLoginView();
            lastPage();

            resetOtherView(true,
                    mStrPasswordLogin,
                    mStrPhoneNum + " " + mPhoneNumber,
                    false, false);

            // 点击返回按钮返回 登录 手机号码输入
            mBackIV.setOnClickListener(v -> backToLoginPhoneInput());
        }
    }

    /**
     * 下一页，验证码登录
     */
    private void nextToVerifyCodeLogin() {
        if (null != getCardView(1)) {
            getCardView(1).loadVerifyCodeLoginView();
            nextPage();

            resetOtherView(true,
                    mStrVerifyCodeLogin,
                    mStrPhoneNum + " " + mPhoneNumber,
                    false, false);

            // 点击返回按钮返回 手机号码登录
            mBackIV.setOnClickListener(v -> backToPasswordLogin());
        }
    }

    /**
     * 下一页，注册 输入验证码
     */
    private void nextToRegisterVerifyCodeInput() {
        if (null != getCardView(1)) {
            getCardView(1).loadRegisterVerifyCodeInputView();
            nextPage();

            resetOtherView(true,
                    mStrInputVerifyCode,
                    String.format(mStrVerifyCodeSentFormat, mPhoneNumber),
                    false,
                    false);

            // 获取注册验证码
            registerSendVerifyCode();

            // 点击返回按钮返回 手机号码登录
            mBackIV.setOnClickListener(v -> backToLoginPhoneInput());
        }
    }

    /**
     * 上一页，注册 输入验证码
     */
    private void backToRegisterVerifyCodeInput() {
        if (null != getCardView(mCardAdapter.getCount() - 1)) {
            getCardView(mCardAdapter.getCount() - 1).loadRegisterVerifyCodeInputView();
            lastPage();

            resetOtherView(true,
                    mStrInputVerifyCode,
                    String.format(mStrVerifyCodeSentFormat, mPhoneNumber),
                    false,
                    false);

            // 点击返回按钮返回 手机号码登录
            mBackIV.setOnClickListener(v -> backToLoginPhoneInput());
        }
    }

    /**
     * 下一页，忘记密码 输入验证码
     */
    private void nextToForgotPasswordVerifyCodeInput() {
        if (null != getCardView(1)) {
            getCardView(1).loadForgotPasswordVerifyCodeInputView();
            nextPage();

            resetOtherView(true,
                    mStrInputVerifyCode,
                    String.format(mStrVerifyCodeSentFormat, mPhoneNumber),
                    false,
                    false);

            // 获取注册验证码
            forgotPasswordSendVerifyCode();

            // 点击返回按钮返回 密码登录
            mBackIV.setOnClickListener(v -> backToPasswordLogin());
        }
    }

    /**
     * 上一页，忘记密码 输入验证码
     */
    private void backToForgotPasswordVerifyCodeInput() {
        if (null != getCardView(mCardAdapter.getCount() - 1)) {
            getCardView(mCardAdapter.getCount() - 1).loadForgotPasswordVerifyCodeInputView();
            lastPage();

            resetOtherView(true,
                    mStrInputVerifyCode,
                    String.format(mStrVerifyCodeSentFormat, mPhoneNumber),
                    false,
                    false);

            // 点击返回按钮返回 密码登录
            mBackIV.setOnClickListener(v -> backToPasswordLogin());
        }
    }


    /**
     * 下一页，重设密码
     */
    private void nextToResetPassword() {
        if (null != getCardView(1)) {
            getCardView(1).loadResetPasswordView();
            nextPage();

            resetOtherView(true,
                    mStrSetPassword,
                    mStrSetPasswordHint,
                    false,
                    false);

            // 点击返回按钮返回 忘记密码 验证码输入
            mBackIV.setOnClickListener(v -> backToForgotPasswordVerifyCodeInput());
        }
    }

    /**
     * 下一页， 注册 输入密码登录
     */
    private void nextToRegisterSetPassword() {
        if (null != getCardView(1)) {
            getCardView(1).loadRegisterSetPasswordView();
            nextPage();

            resetOtherView(true,
                    mStrSetPassword,
                    mStrSetPasswordHint,
                    false,
                    false);

            // 点击返回按钮返回 手机号码登录
            mBackIV.setOnClickListener(v -> backToRegisterVerifyCodeInput());
        }
    }

    /**
     * 下一页， 绑定手机
     */
    private void nextToBindPhone() {
        if (null != getCardView(1)) {
            getCardView(1).loadBindPhoneView();
            nextPage();

            resetOtherView(true,
                    mStrBindPhone,
                    mStrBindPhoneHint,
                    false,
                    false);

            // 点击返回按钮返回 手机号码登录
            mBackIV.setOnClickListener(v -> backToLoginPhoneInput());
        }
    }

    /**
     * 上一页， 绑定手机
     */
    private void backToBindPhone() {
        if (null != getCardView(mCardAdapter.getCount() - 1)) {
            getCardView(mCardAdapter.getCount() - 1).loadBindPhoneView();
            lastPage();

            resetOtherView(true,
                    mStrBindPhone,
                    mStrBindPhoneHint,
                    false,
                    false);

            // 点击返回按钮返回 手机号码登录
            mBackIV.setOnClickListener(v -> backToLoginPhoneInput());
        }
    }

    /**
     * 下一页，绑定手机，验证
     */
    private void nextToBindPhoneVerify() {
        if (null != getCardView(1)) {
            getCardView(1).loadBindPhoneVerifyView();
            nextPage();

            resetOtherView(true,
                    mStrInputVerifyCode,
                    String.format(mStrVerifyCodeSentFormat, mPhoneNumber),
                    false,
                    false);

            // 点击返回按钮返回 手机号码登录
            mBackIV.setOnClickListener(v -> backToBindPhone());
        }
    }

    /**
     * 下一页，绑定手机，验证 无邀请码
     */
    private void nextToBindPhoneVerifyNoInvite() {
        if (null != getCardView(1)) {
            getCardView(1).loadBindPhoneVerifyNoInviteView();
            nextPage();

            resetOtherView(true,
                    mStrInputVerifyCode,
                    String.format(mStrVerifyCodeSentFormat, mPhoneNumber),
                    false,
                    false);

            // 点击返回按钮返回 手机号码登录
            mBackIV.setOnClickListener(v -> backToBindPhone());
        }
    }


    /**
     * 检测手机号码
     *
     * @param phone 手机号码
     * @return 输入的是否是有效的手机号码
     */
    private boolean checkPhoneNumber(String phone) {
        if (TextUtils.isEmpty(phone)) {
            DialogConfirm.show(getSupportFragmentManager(), "提示", "请输入手机号码");

            return false;
        }

        if (!RegexUtils.isMobileExact(phone)) {
            DialogConfirm.show(getSupportFragmentManager(), "提示", "手机号码有误，请核对后重新输入");

            return false;
        }

        return true;
    }

    public static boolean isPassword(String password) {
        String regex = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()\\-_=+<,>.?/;:'\"\\\\|{\\[\\]~·！@#￥%……&*（）——+|{}《》【】‘；：”“'。，、？]){6,18}$";

        return regexMatch(regex, password);
    }

    public static boolean regexMatch(String regex, String str) {
        return !TextUtils.isEmpty(str) && str.matches(regex);
    }

    /**
     * 检测密码
     *
     * @param password 密码
     * @return 密码是否合法
     */
    private boolean checkPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            DialogConfirm.show(getSupportFragmentManager(), "提示", "请输入密码");

            return false;
        }

        if (!isPassword(password)) {
            if (password.length() < 6) {
                DialogConfirm.show(getSupportFragmentManager(), "提示", "密码不能少于6位");
            } else {
                DialogConfirm.show(getSupportFragmentManager(), "提示", "密码只能输入数字、字母和符号");
            }

            return false;
        }

        return true;
    }

    /**
     * 检验验证码
     *
     * @param code 验证码
     * @return 验证码是否合法
     */
    private boolean checkVerifyCode(String code) {
        if (TextUtils.isEmpty(code)) {
            DialogConfirm.show(getSupportFragmentManager(), "提示", "请输入验证码");

            return false;
        }

        if (!isVerifyCode(code)) {
            if (code.length() < 4) {
                DialogConfirm.show(getSupportFragmentManager(), "提示", "验证码不能少于4位");
            } else {
                DialogConfirm.show(getSupportFragmentManager(), "提示", "验证码能输入数字");
            }

            return false;
        }

        return true;
    }

    /**
     * 检验验证码
     *
     * @param code 验证码
     * @return 验证码是否合法
     */
    public static boolean isVerifyCode(String code) {
        String regex = "^\\d{4}$";

        return regexMatch(regex, code);
    }

    /**
     * 登录 手机号码输入页面点击 下一步
     *
     * @param phone 手机号码
     */
    @Override
    public void onPhoneLogin(String phone) {
        if (checkPhoneNumber(phone)) {
            Timber.i("查询手机号码是否已注册");
            mPhoneNumber = phone;
            // 查询手机号码是否已注册
            mPresenter.isRegister(phone);
        }

    }

    /**
     * 验证码登录，获取验证码
     *
     * @param view 倒计时控件
     *             用途类型
     *             （注册 type=1;
     *             找回密码 type=2;
     *             三方账号绑定手机 type=4;
     *             更换手机号 type=5;
     *             短信登录 type=6）
     */
    @Override
    public void onLoginSendVerifyCode(CountDownView view) {
        mCurrentCDV = view;
        // 获取验证码
        mPresenter.sendVerifyCode(mPhoneNumber, Constants.VERIFY_CODE_TYPE_SMS_LOGIN);
    }

    /**
     * 注册，获取验证码
     *
     * @param view 倒计时控件
     *             用途类型
     *             （注册 type=1;
     *             找回密码 type=2;
     *             三方账号绑定手机 type=4;
     *             更换手机号 type=5;
     *             短信登录 type=6）
     */
    @Override
    public void onRegisterSendVerifyCode(CountDownView view) {
        mCurrentCDV = view;
        // 获取验证码
        registerSendVerifyCode();
    }

    /**
     * 获取验证码，用于注册
     */
    private void registerSendVerifyCode() {
        mPresenter.sendVerifyCode(mPhoneNumber, Constants.VERIFY_CODE_TYPE_REGISTER);
    }

    /**
     * 获取验证码，用于忘记密码
     */
    private void forgotPasswordSendVerifyCode() {
        mPresenter.sendVerifyCode(mPhoneNumber, Constants.VERIFY_CODE_TYPE_FORGOT_PASSWORD);
    }

    /**
     * 注册，验证验证码
     *
     * @param code 验证码
     */
    @Override
    public void onRegisterCheckVerifyCode(String code) {
        if (checkVerifyCode(code)) {
            mPresenter.checkVerificationCode(mPhoneNumber, code, Constants.VERIFY_CODE_TYPE_REGISTER);
        }
    }

    /**
     * 忘记密码 获取验证码
     * @param view 倒计时控件
     */
    @Override
    public void onForgotPasswordSendVerifyCode(CountDownView view) {
        mCurrentCDV = view;
        forgotPasswordSendVerifyCode();
    }

    /**
     * 忘记密码 验证验证码
     * @param code 验证码
     */
    @Override
    public void onForgotPasswordCheckVerifyCode(String code) {
        if (checkVerifyCode(code)) {
//            mPresenter.checkVerificationCode(mPhoneNumber, code, Constants.VERIFY_CODE_TYPE_FORGOT_PASSWORD);
            mPresenter.preVerifyForForgetPassword(mPhoneNumber, code);
        }
    }

    /**
     * 手机号码 密码 邀请码 注册
     *
     * @param password       密码
     * @param invitationCode 邀请码
     */
    @Override
    public void onRegisterLogin(String password, String invitationCode) {
        if (checkPassword(password)) {
            mPresenter.register(mPhoneNumber, password, invitationCode);
        }
    }

    @Override
    public void onSwitchToPasswordLogin() {
        // 上一页，密码登录
        backToPasswordLogin();
    }

    @Override
    public void onSwitchToVerifyCodeLogin() {
        // 下一页，验证码登录
        nextToVerifyCodeLogin();
    }

    /**
     * 密码登录
     *
     * @param password 密码
     */
    @Override
    public void onPasswordLogin(String password) {
        if (checkPassword(password)) {
            Timber.i("密码登录");
            mPresenter.passwordLogin(mPhoneNumber, password);
        }
    }

    /**
     * 验证码登录
     *
     * @param code 验证码
     */
    @Override
    public void onVerifyCodeLogin(String code) {
        if (checkVerifyCode(code)) {
            mPresenter.verifyCodeLogin(mPhoneNumber, code);
        }
    }

    /**
     * 第三方登录，绑定手机号码
     *
     * @param phone 需要绑定的手机号码
     */
    @Override
    public void onPlatformBindPhone(String phone) {
//        nextToBindPhoneVerify();

        if (checkPhoneNumber(phone)) {
            mPhoneNumber = phone;
            // 发送验证码
//            mPresenter.bindPhoneSendVerifyCode(phone);

            if (null != mPresenter.getLoginInfo() && null != mPresenter.getLoginInfo().getUserInfo()) {
                mPresenter.checkPhone(mPhoneNumber,
                        mPresenter.getLoginInfo().getUserInfo().getUserId(),
                        mPresenter.getLoginInfo().isFirst());
            } else {
                Timber.e("User info if null");
            }
        }
    }

    /**
     * 绑定手机，获取验证码
     *
     * @param view 倒计时控件
     */
    @Override
    public void onPlatformBindPhoneSendVerifyCode(CountDownView view) {
        mCurrentCDV = view;
        // 获取验证码
        mPresenter.sendVerifyCode(mPhoneNumber, Constants.VERIFY_CODE_TYPE_PLATFORM_BIND_PHONE);
    }

    /**
     * 绑定手机 验证手机号码
     *
     * @param verifyCode 验证码
     * @param inviteCode 邀请码
     */
    @Override
    public void onPlatformBindPhoneVerify(String verifyCode, String inviteCode) {
        if (null != mPresenter.getLoginInfo() && null != mPresenter.getLoginInfo().getUserInfo()) {
            mPresenter.checkCode(mPhoneNumber,
                    mPresenter.getLoginInfo().getUserInfo().getUserId(),
                    verifyCode,
                    inviteCode,
                    mPresenter.getLoginInfo().isFirst());
        } else {
            Timber.e("userinfo is null");
        }
    }

    /**
     * 密码登录，忘记秘密
     */
    @Override
    public void onForgotPassword() {
        Timber.i("忘记密码");
        // 跳转到 忘记密码 验证码输入
        nextToForgotPasswordVerifyCodeInput();
    }

    /**
     * 重设密码
     * @param password 新密码
     */
    @Override
    public void onResetPassword(String password) {
        if (checkPassword(password)) {
            Timber.i("重设密码");
            mPresenter.userForgetPassword(mPhoneNumber, mVerifyCode, password);
        }
    }

    /**
     * 获取指定位置卡片View
     *
     * @param position 位置
     */
    public Login202CardView getCardView(int position) {
        if (null != getCards()) {
            if (position >= 0 && position < getCards().size()) {
                CardItem item = getCards().get(position);
                if (item.view instanceof Login202CardView) {
                    return (Login202CardView) item.view;
                }
            }
        }

        return null;
    }

    /**
     * 卡片列表
     *
     * @return 卡片列表
     */
    public LinkedList<CardItem> getCards() {
        if (null != mCardView) {
            return mCardView.getCards();
        }

        return null;
    }

    /**
     * 下一页（从最前切换到最后）
     */
    private void nextPage() {
        setStyle3();
        mCardView.bringCardToFront(1);
    }

    /**
     * 上一页（从最后切换到最前）
     */
    private void lastPage() {
        setStyle1();
        mCardView.bringCardToFront(mCardAdapter.getCount() - 1);
    }

    /**
     * 卡片切换动画，从最前切换到最后
     */
    private void setStyle1() {
        mCardView.setClickable(false);
        mCardView.setAnimType(InfiniteCardView.ANIM_TYPE_FRONT);
        mCardView.setAnimInterpolator(new LinearInterpolator());
        mCardView.setTransformerToFront(new AnimationTransformer() {
            @Override
            public void transformAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                int positionCount = fromPosition - toPosition;
                float scale = (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount);
                view.setScaleX(scale);
                view.setScaleY(scale);
                if (fraction < 0.5) {
                    view.setTranslationX(cardWidth * fraction * 1.2f);
                    view.setRotationY(-45 * fraction);
                } else {
                    view.setTranslationX(cardWidth * 1.2f * (1f - fraction));
                    view.setRotationY(-45 * (1 - fraction));
                }
            }

            @Override
            public void transformInterpolatedAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                int positionCount = fromPosition - toPosition;
                float scale = (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount);
                float y = -cardHeight * (0.8f - scale) * 0.5f - cardWidth * (0.02f *
                        fromPosition - 0.02f * fraction * positionCount);

                view.setTranslationY(y);
            }
        });
        mCardView.setTransformerToBack(new DefaultTransformerToBack());
        mCardView.setZIndexTransformerToBack(new DefaultZIndexTransformerCommon());
    }

    /**
     * 卡片切换动画，从最后返回最前
     */
    private void setStyle3() {
        mCardView.setClickable(false);
        mCardView.setAnimType(InfiniteCardView.ANIM_TYPE_FRONT_TO_LAST);
        mCardView.setAnimInterpolator(new OvershootInterpolator(-8));
        mCardView.setTransformerToFront(new DefaultCommonTransformer());
        mCardView.setTransformerToBack(new AnimationTransformer() {
            @Override
            public void transformAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                int positionCount = fromPosition - toPosition;
                float scale = (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount);
                view.setScaleX(scale);
                view.setScaleY(scale);
                if (fraction < 0.5) {
                    view.setTranslationX(cardWidth * fraction * 1.2f);
                    view.setRotationY(-45 * fraction);
                } else {
                    view.setTranslationX(cardWidth * 1.2f * (1f - fraction));
                    view.setRotationY(-45 * (1 - fraction));
                }
            }

            @Override
            public void transformInterpolatedAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                int positionCount = fromPosition - toPosition;
                float scale = (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount);
                float y = -cardHeight * (0.8f - scale) * 0.5f - cardWidth * (0.02f *
                        fromPosition - 0.02f * fraction * positionCount);

                view.setTranslationY(y);
            }
        });
        mCardView.setZIndexTransformerToBack(new ZIndexTransformer() {
            @Override
            public void transformAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                if (fraction < 0.5f) {
                    card.zIndex = 1f + 0.01f * fromPosition;
                } else {
                    card.zIndex = 1f + 0.01f * toPosition;
                }
            }

            @Override
            public void transformInterpolatedAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {

            }
        });
    }

    /**
     * 显示加载对话框
     */
    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    /**
     * 隐藏加载对话框
     */
    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    /**
     * 查询是否已注册成功
     *
     * @param isRegister 是否已注册
     */
    @Override
    public void onIsRegisterSuccess(boolean isRegister) {
        if (isRegister) {
            Timber.i("已注册，跳转到密码登录");
            // 已注册，跳转到密码登录
            nextToPasswordLogin();
        } else {
            Timber.i("未注册，跳转到 注册 输入验证码");
            // 未注册，跳转到 注册 输入验证码
            nextToRegisterVerifyCodeInput();
        }
    }

    /**
     * 查询是否已注册失败
     *
     * @param msg 失败信息
     */
    @Override
    public void onIsRegisterFailed(String msg) {
        ToastUtil.showToastShort(msg);
    }

    /**
     * 密码登录成功
     */
    @Override
    public void onPasswordLoginSuccess(LoginInfo loginInfo) {

    }

    /**
     * 密码登录失败
     *
     * @param msg 失败信息
     */
    @Override
    public void onPasswordLoginFailed(String msg) {
        ToastUtil.showToastShort(msg);
    }

    /**
     * 获取验证码成功
     *
     * @param type 用途类型
     *             （注册 type=1;
     *             找回密码 type=2;
     *             三方账号绑定手机 type=4;
     *             更换手机号 type=5;
     *             短信登录 type=6）
     */
    @Override
    public void onSendVerifyCodeSuccess(int type) {
        // 获取验证码成功，开始倒计时
        if (null != mCurrentCDV) {
            mCurrentCDV.start();

            mCurrentCDV = null;
        }
    }

    /**
     * 获取验证码失败
     *
     * @param type 用途类型
     * @param msg  错误信息
     */
    @Override
    public void onSendVerifyCodeFailed(int type, String msg) {
        mCurrentCDV = null;
        ToastUtil.showToastShort(msg);
    }

    /**
     * 验证码登录成功
     *
     * @param loginInfo 用户信息
     */
    @Override
    public void onVerifyCodeLoginSuccess(LoginInfo loginInfo) {

    }

    /**
     * 验证码登录失败
     *
     * @param msg 失败信息
     */
    @Override
    public void onVerifyCodeLoginFailed(String msg) {
        ToastUtil.showToastShort(msg);
    }

    /**
     * 跳转到标签选择
     */
    @Override
    public void gotoTagSelection() {
        Timber.i("跳转到标签选择");
        TagSelectionActivity.startSelectionFromLogin(this);
        finish();
    }

    /**
     * 跳转到主页
     */
    @Override
    public void gotoMainPage() {
        Timber.i("跳转到主页");
        // 路由跳转
        CommonHelper.AppHelper.startHomeActivity(this);
        finish();
    }

    /**
     * 跳转到新用户引导
     */
    @Override
    public void gotoNewUserGuide() {
        Timber.i("跳转到新用户引导");
        CommonHelper.PersonalHelper.startNewGuideActivity(this);
    }

    /**
     * 注册，验证验证码结果
     *
     * @param result 验证结果
     * @param msg    验证结果信息
     */
    @Override
    public void onRegisterCheckVerifyCodeSuccess(boolean result, String msg) {
        if (result) {
            // 验证成功，跳转到设置密码
            nextToRegisterSetPassword();
        } else {
            ToastUtil.showToastShort(msg);
        }
    }

    /**
     * 注册，验证验证码失败
     *
     * @param msg 失败信息
     */
    @Override
    public void onRegisterCheckVerifyCodeFailed(String msg) {
        ToastUtil.showToastShort(msg);
    }

    /**
     * 忘记密码，验证验证码成功
     */
    @Override
    public void onForgotPasswordCheckVerifyCodeSuccess(String mobile, String code) {
        mVerifyCode = code;
        // 跳转到输入密码
        nextToResetPassword();
    }

    /**
     * 忘记密码，验证验证码失败
     * @param msg 失败信息
     */
    @Override
    public void onForgotPasswordCheckVerifyCodeFailed(String msg) {
        ToastUtil.showToastShort(msg);
    }

    /**
     * 注册成功
     *
     * @param loginInfo 登录信息
     */
    @Override
    public void onRegisterSuccess(LoginInfo loginInfo) {
        Timber.i("注册成功");
        gotoNewUserGuide();
    }

    /**
     * 注册失败
     *
     * @param msg 失败信息
     */
    @Override
    public void onRegisterFailed(String msg) {
        ToastUtil.showToastShort(msg);
    }

    @Override
    public Activity getActivity() {
        return Login202Activity.this;
    }

    /**
     * 第三方平台授权失败
     *
     * @param platform 登录平台
     * @param msg      失败信息
     */
    @Override
    public void onThirdPartLoginFailed(int platform, String msg) {
        ToastUtil.showToastShort(msg);
    }

    /**
     * 用户取消第三方平台授权
     *
     * @param platform 登录平台
     */
    @Override
    public void onThirdPartLoginUserCancel(int platform) {
        ToastUtil.showToastShort(mStrUserCancel);
    }

    /**
     * 第三方登录成功
     *
     * @param loginInfo 登录信息
     */
    @Override
    public void onPlatformLoginSuccess(LoginInfo loginInfo) {
        Timber.i("第三方登录成功");

        //  是否绑定了手机号码
        if (loginInfo.isBind()) {
            // 已经绑定了号码，登录成功
            if (null != mPresenter.getLoginInfo()) {
                Timber.i("保存登录信息");
                mPresenter.saveLoginInfo(mPresenter.getLoginInfo());
            }
            Timber.i("获取个人兴趣标签");
            // 已经绑定了手机号码，加载兴趣标签
            mPresenter.myRecommendTag();
        } else {
            // 跳转到绑定手机页面
            Timber.i("绑定手机");
            nextToBindPhone();
        }
    }

    /**
     * 第三方登录失败
     *
     * @param msg 失败信息
     */
    @Override
    public void onPlatformLoginFailed(String msg) {
        ToastUtil.showToastShort(msg);
    }


    /**
     * 绑定手机页面获取验证码成功
     */
    @Override
    public void onCheckPhoneSuccess(CheckPhoneResult result) {
        if (result.getIsFirst() == 0 && result.getIsRegister() == 1) {
            // 不是第一次登陆，并且手机号码已注册
//            ToastUtil.showToastShort("账号和手机号码都已注册过往往账号，无法完成绑定");
            DialogConfirm.show(getSupportFragmentManager(), "提示", "账号和手机号码都已注册过往往账号，无法完成绑定");
        } else if (result.getIsFirst() == 1 && result.getIsRegister() == 0) {
            // 第三方账号第一次登陆，并且手机号码未注册
            // 跳转到 绑定手机验证
            nextToBindPhoneVerify();
        } else {
            // 跳转到 绑定手机验证 无邀请码
            nextToBindPhoneVerifyNoInvite();
        }
    }

    /**
     * 绑定手机页面获取验证码失败
     *
     * @param msg 失败信息
     */
    @Override
    public void onCheckPhoneFailed(String msg) {
        if (msg.contains("不能完成绑定")) {
            DialogConfirm.show(getSupportFragmentManager(),
                    "提示",
                    msg);
        } else {
            ToastUtil.showToastShort(msg);
        }
    }

    /**
     * 绑定手机成功
     */
    @Override
    public void onBindPhoneSuccess() {
        Timber.i("绑定手机成功");
        // 加载兴趣标签
        mPresenter.myRecommendTag();
    }

    @Override
    public void onBindPhoneFailed(String msg) {
        ToastUtil.showToastShort(msg);
    }

    /**
     * 修改密码失败
     * @param msg 失败
     */
    @Override
    public void onUserForgetPasswordFailed(String msg) {
        ToastUtil.showToastShort(msg);
    }

}
