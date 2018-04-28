package com.wang.social.funpoint.mvp.model.api;

import com.frame.component.entities.BaseListWrap;
import com.frame.http.api.BaseJson;
import com.wang.social.funpoint.mvp.entities.Funpoint;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 */

public interface FunpointService {

    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

    /**
     * 趣点列表
     */
    @GET("/app/news/list?v=2.0.0")
    Observable<BaseJson<BaseListWrap<Funpoint>>> getFunpointList(@Query("isCondition") int isCondition, @Query("current") int current, @Query("size") int size);

}
