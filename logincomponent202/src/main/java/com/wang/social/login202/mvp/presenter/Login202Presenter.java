package com.wang.social.login202.mvp.presenter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.frame.component.common.NetParam;
import com.frame.component.entities.location.LocationInfo;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.ui.acticity.tags.TagService;
import com.frame.component.ui.acticity.tags.Tags;
import com.frame.component.ui.acticity.tags.TagsDTO;
import com.frame.component.utils.ChannelUtils;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.integration.IRepositoryManager;
import com.frame.utils.AppUtils;
import com.frame.utils.FrameUtils;
import com.frame.utils.PhoneUtils;
import com.frame.utils.Utils;
import com.wang.social.login202.mvp.model.api.Login202Service;
import com.wang.social.login202.mvp.contract.Login202Contract;
import com.wang.social.login202.mvp.model.entities.CheckPhoneResult;
import com.wang.social.login202.mvp.model.entities.CheckVerifyCode;
import com.wang.social.login202.mvp.model.entities.LoginInfo;
import com.wang.social.login202.mvp.model.entities.dto.CheckPhoneResultDTO;
import com.wang.social.login202.mvp.model.entities.dto.CheckVerifyCodeDTO;
import com.wang.social.login202.mvp.model.entities.dto.IsRegisterDTO;
import com.wang.social.login202.mvp.model.entities.IsRegisterVO;
import com.wang.social.login202.mvp.model.entities.dto.LoginInfoDTO;
import com.wang.social.socialize.SocializeUtil;

import java.util.Map;

import io.reactivex.Observable;
import timber.log.Timber;

public class Login202Presenter implements Login202Contract.Presenter {
    private Login202Contract.View mView;

    private ApiHelper mApiHelper;
    private IRepositoryManager mRepositoryManager;

    // 保存用户信息
    private LoginInfo mLoginInfo;

    public Login202Presenter(@NonNull Login202Contract.View view) {
        mView = view;

        mApiHelper = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).apiHelper();
        mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
    }


    /**
     * 已选推荐标签列表(兴趣标签)
     */
    @Override
    public void myRecommendTag() {
        mApiHelper.execute(mView,
                // 这里需要一次获取所有的标签，所以给一个很大的数字
                netMyRecommendTag(Integer.MAX_VALUE, 0),
                new ErrorHandleSubscriber<Tags>() {

                    @Override
                    public void onNext(Tags tags) {
                        if (tags != null && tags.getList() != null) {
                            doLoginComplete(tags.getList().size() > 0);
                        } else {
                            doLoginComplete(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        doLoginComplete(false);
                    }
                },
                disposable -> mView.showLoading(),
                () -> mView.hideLoading());
    }

    /**
     * 已选兴趣标签列表
     */
    private Observable<BaseJson<TagsDTO>> netMyRecommendTag(int size, int current) {
        Map<String, Object> param = new NetParam()
                .put("size", size)
                .put("current", current)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TagService.class)
                .myRecommendTag(param);
    }


    /**
     * 保存用户信息
     */
    @Override
    public void saveLoginInfo(LoginInfo loginInfo) {
        // 保存数据
        saveLoginInfo(loginInfo, true);
    }

    private void saveLoginInfo(LoginInfo loginInfo, boolean saveUserInfo) {
        // 保存数据
        AppDataHelper.saveToken(loginInfo.getToken());
        AppDataHelper.saveImSign(loginInfo.getImSign());
        if (saveUserInfo) {
            AppDataHelper.saveUser(loginInfo.getUserInfo());
        }
    }


    /**
     * 获取用户信息
     */
    @Override
    public LoginInfo getLoginInfo() {
        return mLoginInfo;
    }

    /**
     * 登录成功
     *
     * @param loginInfo 返回的用户信息
     */
    private void doLoginSuccess(LoginInfo loginInfo) {
        Timber.i("登录成功");

        saveLoginInfo(loginInfo);

        // 获取标签信息
        myRecommendTag();
    }

    private void doRegisterSuccess(LoginInfo loginInfo) {
        saveLoginInfo(loginInfo);
    }

    /**
     * 登录成功后需要判断是否选择了兴趣标签
     *
     * @param hasTags 是否设置了兴趣标签
     */
    private void doLoginComplete(boolean hasTags) {
        if (!hasTags) {
            // 跳转到标签选择页面
            mView.gotoTagSelection();
        } else {
            // 跳转到首页
            mView.gotoMainPage();
        }

    }

    /**
     * 手机号码是否已注册
     *
     * @param mobile 手机号码
     */
    @Override
    public void isRegister(String mobile) {
        mApiHelper.execute(mView,
                netIsRegister(mobile),
                new ErrorHandleSubscriber<IsRegisterVO>() {
                    @Override
                    public void onNext(IsRegisterVO isRegisterVO) {
                        mView.onIsRegisterSuccess(isRegisterVO.isRegister());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onIsRegisterFailed(e.getMessage());
                    }
                },
                disposable -> mView.showLoading(),
                () -> mView.hideLoading());
    }

    /**
     * 查询手机号码是否已注册
     *
     * @param mobile 手机号码
     */
    private Observable<BaseJson<IsRegisterDTO>> netIsRegister(String mobile) {
        Map<String, Object> param = new NetParam()
                .put("mobile", mobile)
                .put("v", "2.0.2")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(Login202Service.class)
                .isRegister(param);
    }

    /**
     * 密码登录
     *
     * @param mobile   手机号码
     * @param password 密码
     */
    @Override
    public void passwordLogin(String mobile, String password) {
        mApiHelper.execute(mView,
                netPasswordLogin(mobile, password),
                new ErrorHandleSubscriber<LoginInfo>() {
                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        mView.onPasswordLoginSuccess(loginInfo);

                        doLoginSuccess(loginInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onPasswordLoginFailed(e.getMessage());
                    }
                },
                disposable -> mView.showLoading(),
                () -> mView.hideLoading());
    }

    /**
     * 密码登录
     *
     * @param mobile   手机号码
     * @param password 密码
     */
    private Observable<BaseJson<LoginInfoDTO>> netPasswordLogin(String mobile, String password) {
        Map<String, Object> param = new NetParam()
                .put("mobile", mobile)
                .put("password", password)
                .put("devicesKey", PhoneUtils.getIMEI())
                .putStaticParam()
                .put("v", "2.0.2")
                .putSignature()
                .build();
        return mRepositoryManager
                .obtainRetrofitService(Login202Service.class)
                .passwordLogin(param);
    }

    /**
     * 获取验证码
     */
    @Override
    public void sendVerifyCode(String mobile, int type) {
        mApiHelper.executeForData(mView,
                netSendVerifyCode(mobile, type),
                new ErrorHandleSubscriber() {
                    @Override
                    public void onNext(Object o) {
                        mView.onSendVerifyCodeSuccess(type);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onSendVerifyCodeFailed(type, e.getMessage());
                    }
                },
                disposable -> mView.showLoading(),
                () -> mView.hideLoading());
    }

    /**
     * 获取验证码
     *
     * @param mobile 手机号码
     * @param type   用途类型
     *               （注册 type=1;
     *               找回密码 type=2;
     *               三方账号绑定手机 type=4;
     *               更换手机号 type=5;
     *               短信登录 type=6）
     */
    private Observable<BaseJson> netSendVerifyCode(String mobile, int type) {
        Map<String, Object> param = new NetParam()
                .put("mobile", mobile)
                .put("type", type)
                .put("v", "2.0.0")
                .putSignature()
                .build();
        return mRepositoryManager
                .obtainRetrofitService(Login202Service.class)
                .sendVerifyCode(param);
    }

    /**
     * 手机号码加短信验证码登录
     *
     * @param mobile 手机号码
     * @param code   短信验证码
     */
    @Override
    public void verifyCodeLogin(String mobile, String code) {
        mApiHelper.execute(mView,
                netVerifyCodeLogin(mobile, code, ""),
                new ErrorHandleSubscriber<LoginInfo>() {

                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        mView.onVerifyCodeLoginSuccess(loginInfo);

                        doLoginSuccess(loginInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onVerifyCodeLoginFailed(e.getMessage());
                    }

                },
                disposable -> mView.showLoading(),
                () -> mView.hideLoading());
    }

    /**
     * 手机号码加短信验证码登录
     *
     * @param mobile 手机号码
     * @param code   短信验证码
     * @param adCode 区域编码
     */
    private Observable<BaseJson<LoginInfoDTO>> netVerifyCodeLogin(
            String mobile, String code, String adCode) {
        Map<String, Object> param = new NetParam()
                .put("mobile", mobile)
                .put("code", code)
                .put("adCode", adCode)
                .put("devicesKey", PhoneUtils.getIMEI())
                .put("v", "2.0.2")
                .putStaticParam()
                .putSignature()
                .build();
        return mRepositoryManager
                .obtainRetrofitService(Login202Service.class)
                .verifyCodeLogin(param);
    }


    /**
     * 判断验证码
     *
     * @param mobile           手机号码
     * @param verificationCode 验证码
     */
    @Override
    public void checkVerificationCode(String mobile, String verificationCode) {
        mApiHelper.execute(mView,
                netCheckVerificationCode(mobile, verificationCode),
                new ErrorHandleSubscriber<CheckVerifyCode>() {
                    @Override
                    public void onNext(CheckVerifyCode checkVerifyCode) {
                        mView.onRegisterCheckVerifyCodeSuccess(checkVerifyCode.isOK(), checkVerifyCode.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onRegisterCheckVerifyCodeFailed(e.getMessage());
                    }
                },
                disposable -> mView.showLoading(),
                () -> mView.hideLoading());
    }

    /**
     * 判断验证码
     *
     * @param mobile           手机号码
     * @param verificationCode 验证码
     */
    private Observable<BaseJson<CheckVerifyCodeDTO>> netCheckVerificationCode(String mobile, String verificationCode) {
        Map<String, Object> param = new NetParam()
                .put("mobile", mobile)
                .put("code", verificationCode)
                .put("v", "2.0.2")
                .putSignature()
                .build();
        return mRepositoryManager
                .obtainRetrofitService(Login202Service.class)
                .checkVerificationCode(param);
    }

    /**
     * 邀请码和密码注册
     *
     * @param mobile         手机号码
     * @param password       密码
     * @param invitationCode 邀请码
     */
    @Override
    public void register(String mobile, String password, String invitationCode) {
        mApiHelper.execute(mView,
                netUserRegister(mobile, password, invitationCode),
                new ErrorHandleSubscriber<LoginInfo>() {
                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        // 注册成功则后台已经登录
                        doRegisterSuccess(loginInfo);

                        mView.onRegisterSuccess(loginInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onRegisterFailed(e.getMessage());
                    }
                },
                disposable -> mView.showLoading(),
                () -> mView.hideLoading());
    }

    /**
     * 邀请码和密码注册
     *
     * @param mobile         手机号码
     * @param password       密码
     * @param invitationCode 邀请码
     */
    private Observable<BaseJson<LoginInfoDTO>> netRegister(String mobile, String password, String invitationCode) {
        Map<String, Object> param = new NetParam()
                .put("mobile", mobile)
                .put("password", password)
                .put("inviteCode", TextUtils.isEmpty(invitationCode) ? null : invitationCode)
                .put("v", "2.0.2")
//                .putSignature()
                .build();
        return mRepositoryManager
                .obtainRetrofitService(Login202Service.class)
                .register(param);
    }

    /**
     *
     * 参数名字	参数类型	说明	是否必须
     mobile	String	十一位手机号码	√
     code	String	短信验证码	√
     password	String	设置的密码	√
     adCode	String	区域编码	×

     devicesKey	String	设备唯一标识	√
     channelId	Integer	渠道	√
     appVersion	String	APP版本	√
     devicesModel	String	设备机型	√
     devicesSystem	String	设备操作系统	√
     longitude	String	经度
     latitude	String	纬度
     */
    private Observable<BaseJson<LoginInfoDTO>> netUserRegister(
            String mobile, String password, String invitationCode) {
        Map<String, Object> param = NetParam.newInstance()
                .put("mobile", mobile)
                .put("password", password)
                .put("invitationCode", invitationCode)
                .put("adCode", "")
                .putStaticParam()
                .put("v", "2.0.2")
                .build();

        return mRepositoryManager
                .obtainRetrofitService(Login202Service.class)
                .register(param);
    }




    // 第三方登录回调
    private SocializeUtil.LoginListener loginListener = new SocializeUtil.LoginListener() {
        @Override
        public void onStart(int platform) {
            mView.showLoading();
        }

        @Override
        public void onComplete(int platform, Map<String, String> data, String uid, String nickname, int sex, String headUrl) {
            Timber.i(
                    String.format(
                            "授权成功 : %d %s %s %s %d \n %s",
                            platform, platform, uid, nickname, sex, headUrl));

            // 授权登录成功，调用往往第三方登录接口
            platformLogin(platform,
                    uid,
                    TextUtils.isEmpty(nickname) ? "W-NewType" : nickname,
                    TextUtils.isEmpty(headUrl) ? "http://static.wangsocial.com/userDefault.png" : headUrl,
                    sex);
        }

        @Override
        public void onError(int platform, Throwable t) {
            mView.onThirdPartLoginFailed(platform, t.getMessage());
            mView.hideLoading();
        }

        @Override
        public void onCancel(int platform) {
            mView.onThirdPartLoginUserCancel(platform);
            mView.hideLoading();
        }
    };

    /**
     * 微信登录
     */
    @Override
    public void wxLogin() {
        SocializeUtil.wxLogin(mView.getActivity(), loginListener);
    }

    /**
     * QQ登录
     */
    @Override
    public void qqLogin() {
        SocializeUtil.qqLogin(mView.getActivity(), loginListener);
    }

    /**
     * 新浪微博登录
     */
    @Override
    public void sinaLogin() {
        SocializeUtil.sinaLogin(mView.getActivity(), loginListener);
    }

    /**
     * 第三方平台登录
     * @param platform 用户绑定数据类型，2.微信注册 3.qq注册 4新浪微博
     * @param uid 对应平台ID
     * @param nickname 昵称
     * @param headUrl 头像
     * @param sex 性别 0 男 1女
     */
    private void platformLogin(int platform, String uid, String nickname, String headUrl, int sex) {
        mApiHelper.execute(mView,
                netPlatformLogin(platform,
                        uid,
                        nickname,
                        headUrl,
                        sex,
                        ""),
                new ErrorHandleSubscriber<LoginInfo>() {
                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        // 保存用户信息
                        // 不能直接保存用户信息，否则未绑定手机再次启动会认为已登录
                        saveLoginInfo(loginInfo, false);
                        // 记录用户信息，如果需要
                        mLoginInfo = loginInfo;

                        mView.onPlatformLoginSuccess(loginInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onPlatformLoginFailed(e.getMessage());
                    }
                },
                disposable -> mView.showLoading(),
                () -> mView.hideLoading());
    }

    /**
     * 第三方平台登录
     * @param platform 用户绑定数据类型，2.微信注册 3.qq注册 4新浪微博
     * @param uid 对应平台ID
     * @param nickname 昵称
     * @param headUrl 头像
     * @param sex 性别 0 男 1女
     * @param adCode 区域编码
     */
    private Observable<BaseJson<LoginInfoDTO>> netPlatformLogin(
            int platform, String uid, String nickname, String headUrl, int sex, String adCode) {
        Map<String, Object> param = new NetParam()
                .put("platform", platform)
                .put("uid", uid)
                .put("nickname", nickname)
                .put("headUrl", headUrl)
                .put("sex", sex)
                .put("adCode", adCode)
                .putStaticParam()
                .put("v", "2.0.2")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(Login202Service.class)
                .platformLogin(param);
    }

    /**
     *
     * 2.4.1未绑定时输入手机号码获取验证码
     * 根据手机号码判断该号码是否被绑定
     * @param phone 手机号码
     * @param userId 用户id
     */
    @Override
    public void checkPhone(String phone, int userId, boolean isFirst) {
        mApiHelper.execute(mView,
                netCheckPhone(phone, userId, isFirst ? 1 : 0),
                new ErrorHandleSubscriber<CheckPhoneResult>() {
                    @Override
                    public void onNext(CheckPhoneResult checkPhoneResult) {
                        mView.onCheckPhoneSuccess(checkPhoneResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onCheckPhoneFailed(e.getMessage());
                    }
                },
                disposable -> mView.showLoading(),
                () -> mView.hideLoading());
    }

    /**
     *
     * 2.4.1未绑定时输入手机号码获取验证码
     * 根据手机号码判断该号码是否被绑定
     * @param phone 手机号码
     * @param userId 用户id
     * @param isFirst 是否第一次登陆(0：否；1：是)
     */
    private Observable<BaseJson<CheckPhoneResultDTO>> netCheckPhone(String phone, int userId, int isFirst) {
        Map<String, Object> param = new NetParam()
                .put("phone", phone)
                .put("userId", userId)
                .put("isFirst", isFirst)
                .put("v", "2.0.2")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(Login202Service.class)
                .checkPhone(param);
    }

    /**
     * 判断验证码和邀请码绑定手机
     * @param phone 手机号码
     * @param userId 用户id
     * @param code 验证码
     * @param inviteCode 邀请码
     */
    @Override
    public void checkCode(String phone, int userId, String code, String inviteCode, boolean isFirst) {
        mApiHelper.execute(mView,
                netCheckCode(phone, userId, code, inviteCode, isFirst ? 1 : 0),
                new ErrorHandleSubscriber<LoginInfo>() {
                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        saveLoginInfo(loginInfo);
                        // 绑定成功
                        mView.onBindPhoneSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onBindPhoneFailed(e.getMessage());
                    }
                },
                disposable -> mView.showLoading(),
                () -> mView.hideLoading());
    }

    /**
     * 判断验证码和邀请码绑定手机
     * @param phone 手机号码
     * @param userId 用户id
     * @param code 验证码
     * @param inviteCode 邀请码
     */
    private Observable<BaseJson<LoginInfoDTO>> netCheckCode(String phone, int userId, String code, String inviteCode, int isFirst) {
        Map<String, Object> param = new NetParam()
                .put("phone", phone)
                .put("userId", userId)
                .put("code", code)
                .put("inviteCode", inviteCode)
                .put("isFirst", isFirst)
                .put("v", "2.0.2")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(Login202Service.class)
                .checkCode(param);
    }

}
