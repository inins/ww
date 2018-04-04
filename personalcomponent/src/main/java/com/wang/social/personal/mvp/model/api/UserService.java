package com.wang.social.personal.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.wang.social.personal.mvp.entities.CommonEntity;
import com.wang.social.personal.mvp.entities.QiniuTokenWrap;
import com.wang.social.personal.mvp.entities.UserWrap;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 */

public interface UserService {

    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

    /**
     * 登陆
     */
    @FormUrlEncoded
    @POST("/login/password")
    Observable<BaseJson<UserWrap>> login(@Field("mobile") String mobile, @Field("password") String password, @Field("v") String v);

    /**
     * 获取七牛云上传token
     */
    @POST("/common/upToken")
    Observable<BaseJson<QiniuTokenWrap>> getQiniuToken();

    /**
     * 修改个人信息
     * nickname
     * avatar
     * sex
     * birthday
     * autograph
     * province
     * city
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("/app/userInfo/changeInfo")
    Observable<BaseJson<CommonEntity>> updateUserInfo(@FieldMap Map<String, Object> param);
}
