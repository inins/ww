package com.wang.social.login202.mvp.model.api;

import com.frame.component.ui.acticity.tags.TagsDTO;
import com.frame.http.api.BaseJson;
import com.wang.social.login202.mvp.model.entities.dto.CheckPhoneResultDTO;
import com.wang.social.login202.mvp.model.entities.dto.CheckVerifyCodeDTO;
import com.wang.social.login202.mvp.model.entities.dto.IsRegisterDTO;
import com.wang.social.login202.mvp.model.entities.dto.LoginInfoDTO;
import com.wang.social.login202.mvp.model.entities.dto.PlatformLoginDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


public interface Login202Service {


    /**
     * 判断用户是否注册
     */
    @GET("login/isRegister")
    Observable<BaseJson<IsRegisterDTO>> isRegister(@QueryMap Map<String, Object> param);

    /**
     * 密码登陆
     */
    @FormUrlEncoded
    @POST("login/password")
    Observable<BaseJson<LoginInfoDTO>> passwordLogin(@FieldMap Map<String, Object> param);

    /**
     * 获取验证码
     *
     用途类型
     （注册 type=1;
     找回密码 type=2;
     三方账号绑定手机 type=4;
     更换手机号 type=5;
     短信登录 type=6）
     */
    @FormUrlEncoded
    @POST("common/sendVerifyCode")
    Observable<BaseJson> sendVerifyCode(@FieldMap Map<String, Object> param);


    /**
     * 手机号码加短信验证码登录
     */
    @FormUrlEncoded
    @POST("login/smsCode")
    Observable<BaseJson<LoginInfoDTO>> verifyCodeLogin(@FieldMap Map<String, Object> param);


    /**
     * 判断用户是否注册
     */
    @GET("login/checkVerificationCode")
    Observable<BaseJson<CheckVerifyCodeDTO>> checkVerificationCode(@QueryMap Map<String, Object> param);

    /**
     * 邀请码和密码注册
     */
    @FormUrlEncoded
    @POST("login/register")
    Observable<BaseJson<LoginInfoDTO>> register(@FieldMap Map<String, Object> param);

    /**
     * 手机号码注册
     */
    @FormUrlEncoded
    @POST("user/register")
    Observable<BaseJson<LoginInfoDTO>> userRegister(@FieldMap Map<String, Object> param);

    /**
     * 第三方登录
     */
    @FormUrlEncoded
    @POST("login/platform")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    Observable<BaseJson<LoginInfoDTO>> platformLogin(@FieldMap Map<String, Object> param);

    /**
     * 2.4.1未绑定时输入手机号码获取验证码
     * 根据手机号码判断该号码是否被绑定
     */
    @GET("app/userInfo/checkPhone")
    Observable<BaseJson<CheckPhoneResultDTO>> checkPhone(@QueryMap Map<String, Object> param);


    /**
     * 判断验证码和邀请码绑定手机
     */
    @FormUrlEncoded
    @POST("app/userInfo/checkCode")
    Observable<BaseJson<LoginInfoDTO>> checkCode(@FieldMap Map<String, Object> param);

    /**
     * 修改/重置密码（前置验证）验证验证码
     */
    @FormUrlEncoded
    @POST("user/preVerifyForForgetPassword")
    Observable<BaseJson> preVerifyForForgetPassword(@FieldMap Map<String, Object> param);


    /**
     * 修改/重置密码（公共）
     */
    @FormUrlEncoded
    @POST("user/forgetPassword")
    Observable<BaseJson> userForgetPassword(@FieldMap Map<String, Object> param);
}
