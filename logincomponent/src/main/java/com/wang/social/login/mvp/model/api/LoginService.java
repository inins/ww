package com.wang.social.login.mvp.model.api;

import com.wang.social.login.mvp.model.entities.dto.LoginInfoDTO;
import com.frame.http.api.BaseJson;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
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
     * @return
     */
    @FormUrlEncoded
    @POST("login/password")
    Observable<BaseJson<LoginInfoDTO>> passwordLogin(@FieldMap Map<String, Object> param);


    /**
     * 手机号码加短信验证码登录
     * @return
     */
    @FormUrlEncoded
    @POST("login/smsCode")
    Observable<BaseJson<LoginInfoDTO>> verifyCodeLogin(@FieldMap Map<String, Object> param);


    /**
     * 手机号码加短信验证码注册用户
     * @return
     */
    @FormUrlEncoded
    @POST("user/register")
    Observable<BaseJson> userRegister(@FieldMap Map<String, Object> param);

    /**
     * 修改/重置密码（前置验证）验证验证码
     * @return
     */
    @FormUrlEncoded
    @POST("user/preVerifyForForgetPassword")
    Observable<BaseJson> preVerifyForForgetPassword(@FieldMap Map<String, Object> param);


    /**
     * 修改/重置密码（公共）
     * @return
     */
    @FormUrlEncoded
    @POST("user/forgetPassword")
    Observable<BaseJson> userForgetPassword(@FieldMap Map<String, Object> param);

    /**
     *  设置密码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("app/userInfo/setPassword")
    Observable<BaseJson> userSetPassword(@FieldMap Map<String, Object> param);

    /**
     * 手机号码加短信验证码登录
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
    Observable<BaseJson> sendVerifyCode(@FieldMap Map<String, Object> param);


    /**
     * 换绑手机
     *
     * @return
     */
    @FormUrlEncoded
    @POST("app/userInfo/replaceMobile")
    Observable<BaseJson> replaceMobile(@FieldMap Map<String, Object> param);


    /**
     * 第三方登录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("login/platform")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    Observable<BaseJson<LoginInfoDTO>> platformLogin(@FieldMap Map<String, Object> param);
}
