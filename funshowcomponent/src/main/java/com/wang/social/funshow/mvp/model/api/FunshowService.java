package com.wang.social.funshow.mvp.model.api;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.UserWrap;
import com.frame.http.api.BaseJson;
import com.wang.social.funshow.mvp.entities.Funshow;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 */

public interface FunshowService {

    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

    /**
     * 登陆
     * mobile
     * password
     * nonceStr
     * signature
     */
    @GET("/app/talk/getTalkList")
    Observable<BaseJson<BaseListWrap<Funshow>>> getFunshowList(@Query("type") int type, @Query("current") int current, @Query("size") int size);

}
