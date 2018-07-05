package com.wang.social.login202.mvp.contract;

import android.app.Activity;

import com.frame.mvp.IView;
import com.wang.social.login202.mvp.model.entities.CheckPhoneResult;
import com.wang.social.login202.mvp.model.entities.CheckVerifyCode;
import com.wang.social.login202.mvp.model.entities.LoginInfo;
import com.wang.social.login202.mvp.util.Constants;

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
         * 忘记密码，验证验证码成功
         */
        void onForgotPasswordCheckVerifyCodeSuccess(String mobile, String code);

        /**
         * 忘记密码，验证验证码失败
         * @param msg 失败信息
         */
        void onForgotPasswordCheckVerifyCodeFailed(String msg);

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
         * @param loginInfo 用户登录信息
         */
        void onPlatformLoginSuccess(LoginInfo loginInfo);

        /**
         * 第三方平台登录失败
         * @param msg 失败信息
         */
        void onPlatformLoginFailed(String msg);

        /**
         * 绑定手机页面获取验证码成功
         */
        void onCheckPhoneSuccess(CheckPhoneResult result);

        /**
         * 绑定手机页面获取验证码失败
         * @param msg 失败信息
         */
        void onCheckPhoneFailed(String msg);

        /**
         * 绑定手机成功
         */
        void onBindPhoneSuccess();

        /**
         * 绑定手机失败
         * @param msg 失败信息
         */
        void onBindPhoneFailed(String msg);

        /**
         * 重置密码失败
         * @param msg 失败
         */
        void onUserForgetPasswordFailed(String msg);
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
        void sendVerifyCode(String mobile, @Constants.VerifyCodeType int type);

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
        void checkVerificationCode(String mobile, String verificationCode, @Constants.VerifyCodeType int type);


        /**
         * 邀请码和密码注册
         * @param mobile 手机号码
         * @param password 密码
         * @param invitationCode 邀请码
         */
        void register(String mobile, String password, String invitationCode);

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

        /**
         * 保存登录返回的用户信息
         * @param loginInfo 用户信息
         */
        void saveLoginInfo(LoginInfo loginInfo);

        /**
         * 获取用户信息
         */
        LoginInfo getLoginInfo();

        /**
         * 2.4.1未绑定时输入手机号码获取验证码
         * 根据手机号码判断该号码是否被绑定
         * @param phone 手机号码
         * @param userId 用户id
         * @param isFirst 是否第一次登陆
         */
        void checkPhone(String phone, int userId, boolean isFirst);

        /**
         * 判断验证码和邀请码绑定手机
         * @param phone 手机号码
         * @param userId 用户id
         * @param code 验证码
         * @param inviteCode 邀请码
         * @param isFirst 是否第一次登陆
         */
        void checkCode(String phone, int userId, String code, String inviteCode, boolean isFirst);

        /**
         *
         * 修改/重置密码（前置验证）验证验证码
         * @param mobile 手机好吗
         * @param code 验证码
         */
        void preVerifyForForgetPassword(String mobile, String code);

        /**
         * 修改/重置密码（公共）
         * @param mobile 手机号码
         * @param code 验证码
         * @param password 密码
         */
        void userForgetPassword(String mobile, String code, String password);
    }
}
