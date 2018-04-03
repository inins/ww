package com.wang.social.personal.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.wang.social.personal.mvp.entities.UserWrap;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 */

public interface UserService {

    /**
     * 登陆
     * @param mobile
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("/login/password")
    Observable<BaseJson<UserWrap>> login(@Field("mobile") String mobile, @Field("password") String password);
}
