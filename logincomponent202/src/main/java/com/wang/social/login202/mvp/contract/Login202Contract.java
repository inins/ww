package com.wang.social.login202.mvp.contract;

import android.app.Activity;

import com.frame.mvp.IView;
import com.wang.social.login202.mvp.model.entities.LoginInfo;

public interface Login202Contract {
    interface View extends IView {
        /**
         * 是否已注册返回
         * @param isRegister 是否已注册
         */
        void onIsRegisterSuccess(boolean isRegister);

        /**
         * 是否已注册查询失败
         * @param msg 失败信息
         */
        void onIsRegisterFailed(String msg);

        /**
         * 密码登录成功
         */
        void onPasswordLoginSuccess(LoginInfo loginInfo);

        /**
         * 密码登录失败
         * @param msg 失败信息
         */
        void onPasswordLoginFailed(String msg);

        /**
         * 获取验证码成功
         * @param type 用途类型
         */
        void onSendVerifyCodeSuccess(int type);

        /**
         * 获取验证码失败
         * @param type 用途类型
         * @param msg 错误信息
         */
        void onSendVerifyCodeFailed(int type, String msg);

        /**
         * 验证码登录成功
         * @param loginInfo 用户信息
         */
        void onVerifyCodeLoginSuccess(LoginInfo loginInfo);

        /**
         * 验证码登录失败
         * @param msg 失败信息
         */
        void onVerifyCodeLoginFailed(String msg);

        /**
         * 跳转到标签选择
         */
        void gotoTagSelection();

        /**
         * 跳转到主页
         */
        void gotoMainPage();

        /**
         * 跳转到新用户引导
         */
        void gotoNewUserGuide();

        /**
         * 注册，验证验证码成功
         * @param result 验证结果
         * @param msg 验证结果信息
         */
        void onRegisterCheckVerifyCodeSuccess(boolean result, String msg);

        /**
         * 注册，验证验证码失败
         * @param msg 错误信息
         */
        void onRegisterCheckVerifyCodeFailed(String msg);

        /**
         * 注册成功
         * @param loginInfo 登录信息
         */
        void onRegisterSuccess(LoginInfo loginInfo);

        /**
         * 注册失败
         * @param msg 失败信息
         */
        void onRegisterFailed(String msg);

        Activity getActivity();

        /**
         * 第三方登录失败
         * @param platform 登录平台
         * @param msg 失败信息
         */
        void onThirdPartLoginFailed(int platform, String msg);

        /**
         * 第三方登录用户取消
         * @param platform 登录平台
         */
        void onThirdPartLoginUserCancel(int platform);

        /**
         * 第三方平台登录成功
         * @param phone 手机号码
         */
        void onPlatformLoginSuccess(String phone);

        /**
         * 第三方平台登录失败
         * @param msg 失败信息
         */
        void onPlatformLoginFailed(String msg);

        /**
         * 绑定手机页面获取验证码成功
         */
        void onBindPhoneSendVerifyCodeSuccess();

        /**
         * 绑定手机页面获取验证码失败
         * @param msg 失败信息
         */
        void onBindPhoneSendVerifyCodeFailed(String msg);

        /**
         * 绑定手机成功
         */
        void onBindPhoneSuccess();

        /**
         * 绑定手机失败
         * @param msg 失败信息
         */
        void onBindPhoneFailed(String msg);
    }

    interface Presenter {
        /**
         * 判断手机号码是否已注册
         * @param phone 手机号码
         */
        void isRegister(String phone);

        /**
         * 密码登录
         * @param mobile 手机号码
         * @param password 密码
         */
        void passwordLogin(String mobile, String password);

        /**
         * 获取验证码
         * @param mobile 手机号码
         * @param type   用途类型
         *               （注册 type=1;
         *               找回密码 type=2;
         *               三方账号绑定手机 type=4;
         *               更换手机号 type=5;
         *               短信登录 type=6）
         */
        void sendVerifyCode(String mobile, int type);

        /**
         * 手机号码加短信验证码登录
         * @param mobile 手机号码
         * @param code 短信验证码
         */
        void verifyCodeLogin(String mobile, String code);

        /**
         * 判断验证码
         * @param mobile 手机号码
         * @param verificationCode 验证码
         */
        void checkVerificationCode(String mobile, String verificationCode);


        /**
         * 邀请码和密码注册
         * @param mobile 手机号码
         * @param password 密码
         * @param invitationCode 邀请码
         */
        void register(String mobile, String password, String invitationCode);

        /**
         * 绑定手机输入手机号码页面获取验证码
         * @param mobile 手机号码
         */
        void bindPhoneSendVerifyCode(String mobile);

        /**
         * 绑定手机
         * @param mobile 手机号码
         * @param code 验证码
         */
        void replaceMobile(String mobile, String code);

        /**
         * 第三方登录
         */
        void wxLogin();
        void qqLogin();
        void sinaLogin();

        /**
         * 我的兴趣标签
         */
        void myRecommendTag();
    }
}
