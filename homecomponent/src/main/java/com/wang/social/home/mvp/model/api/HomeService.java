package com.wang.social.home.mvp.model.api;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.Tag;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.http.api.BaseJson;
import com.google.gson.JsonObject;
import com.wang.social.home.mvp.entities.Funshow;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 */

public interface HomeService {

    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

    /**
     * 首页趣点列表+话题列表
     * isCondition 是否为大量知识(0:是，1：不是)
     */
    @GET("/app/news/getNewsAndTopicsForIndex?v=2.0.0")
    Observable<BaseJson<BaseListWrap<JsonObject>>> getFunpointAndTopic(@Query("isCondition") int isCondition, @Query("current") int current, @Query("size") int size);

    /**
     * 首页最新趣晒
     */
    @GET("/app/talk/latest?v=2.0.0")
    Observable<BaseJson<BaseListWrap<Funshow>>> getNewFunshow();

//    /**
//     * 阅读趣点数量统计
//     */
//    @FormUrlEncoded
//    @POST("/app/news/addRealTotal?v=2.0.0")
//    Observable<BaseJson<Object>> readFunpoint(@Field("newsId") int newsId);

}
