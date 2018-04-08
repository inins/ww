package com.wang.social.login.mvp.model.api;

import com.wang.social.login.mvp.model.entities.dto.LoginInfoDTO;
import com.frame.http.api.BaseJson;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * =========================================
 * 登陆相关Api
 * <p>
 * Create by ChenJing on 2018-03-20 17:35
 * =========================================
 */

public interface LoginService {

    /**
     * 密码登陆
     * @param mobile 手机号码
     * @param password 密码
     * @Param sign 签名
     * @return
     */
    @FormUrlEncoded
    @POST("login/password")
    Observable<BaseJson<LoginInfoDTO>> passwordLogin(
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("sign") String sign);


    /**
     * 手机号码加短信验证码登录
     * @param mobile 手机号码
     * @param code 短信验证码
     * @param sign 签名
     * @param adCode 区域编码
     * @return
     */
    @FormUrlEncoded
    @POST("login/smsCode")
    Observable<BaseJson<LoginInfoDTO>> verifyCodeLogin(
            @Field("mobile") String mobile,
            @Field("code") String code,
            @Field("sign") String sign,
            @Field("adCode") String adCode);


    /**
     * 手机号码加短信验证码注册用户
     * @param mobile 手机号
     * @param code 短信验证码
     * @param password 设置的密码
     * @param adCode 区域编码
     * @return
     */
    @FormUrlEncoded
    @POST("user/register")
    Observable<BaseJson> userRegister(
            @Field("mobile") String mobile,
            @Field("code") String code,
            @Field("password") String password,
            @Field("adCode") String adCode);

    /**
     * 修改/重置密码（前置验证）验证验证码
     * @param mobile
     * @param code
     * @return
     */
    @FormUrlEncoded
    @POST("user/preVerifyForForgetPassword")
    Observable<BaseJson> preVerifyForForgetPassword(
            @Field("mobile") String mobile,
            @Field("code") String code);


    /**
     * 修改/重置密码（公共）
     * @param mobile
     * @param code
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("user/forgetPassword")
    Observable<BaseJson> userForgetPassword(
            @Field("mobile") String mobile,
            @Field("code") String code,
            @Field("password") String password);

    /**
     * 手机号码加短信验证码登录
     * @param mobile 手机号码
     * @param type 用途类型
     * @param sign 签名
     *
     用途类型
    （注册 type=1;
    找回密码 type=2;
    三方账号绑定手机 type=4;
    更换手机号 type=5;
    短信登录 type=6）
     * @return
     */
    @FormUrlEncoded
    @POST("common/sendVerifyCode")
    Observable<BaseJson> sendVerifyCode(
            @Field("mobile") String mobile,
            @Field("type") int type,
            @Field("sign") String sign);


}
