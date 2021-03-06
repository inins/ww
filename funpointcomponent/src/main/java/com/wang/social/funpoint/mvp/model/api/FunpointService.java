package com.wang.social.funpoint.mvp.model.api;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.Tag;
import com.frame.http.api.BaseJson;
import com.frame.component.entities.funpoint.Funpoint;

import io.reactivex.Observable;
import retrofit2.http.Field;
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

    /**
     * 搜索趣点
     */
    @Headers(HEADER_CONTENT_TYPE)
    @GET("/app/news/searchList?v=2.0.2")
    Observable<BaseJson<BaseListWrap<Funpoint>>> getSearchFunpointList(@Query("key") String key, @Query("current") int current, @Query("size") int size);

//    /**
//     * 阅读趣点数量统计
//     */
//    @FormUrlEncoded
//    @POST("/app/news/addRealTotal?v=2.0.0")
//    Observable<BaseJson<Object>> readFunpoint(@Field("newsId") int newsId);

    /**
     * 已选推荐标签
     */
    @POST("/app/tag/myRecommendTag?v=2.0.0")
    Observable<BaseJson<BaseListWrap<Tag>>> getRecommendTag();
}
