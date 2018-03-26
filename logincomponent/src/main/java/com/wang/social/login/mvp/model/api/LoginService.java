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
     * 登陆
     * @param mobile
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("login")
    Observable<BaseJson<LoginInfoDTO>> login(@Field("mobile") String mobile, @Field("password") String password);
}
