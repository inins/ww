package com.wang.social.home.mvp.model.api;

import com.frame.component.entities.BaseCardListWrap;
import com.frame.component.entities.BaseListWrap;
import com.frame.http.api.BaseJson;
import com.google.gson.JsonObject;
import com.wang.social.home.mvp.entities.card.CardGroup;
import com.wang.social.home.mvp.entities.card.CardUser;
import com.wang.social.home.mvp.entities.funshow.FunshowHome;
import com.wang.social.home.mvp.entities.funshow.FunshowHomeDetail;
import com.wang.social.home.mvp.entities.topic.TopicHomeDetail;
import com.wang.social.home.mvp.entities.user.RecommendUser;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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
    @GET("/app/talk/latest?v=2.0.2")
    Observable<BaseJson<BaseListWrap<FunshowHome>>> getNewFunshow();

    /**
     * 首页推荐用户
     */
    @GET("/app/interest/searchUser?v=2.0.2")
    Observable<BaseJson<BaseListWrap<RecommendUser>>> getRecommendUsers();

    /**
     * 首页卡牌，有趣的同类
     */
    @GET("/app/interest/searchUser?v=2.0.0")
    Observable<BaseJson<BaseCardListWrap<CardUser>>> getCardUsers(@QueryMap Map<String, Object> map);

    /**
     * 首页卡牌，有趣的圈子
     */
    @GET("/app/interest/searchGroup?v=2.0.0")
    Observable<BaseJson<BaseCardListWrap<CardGroup>>> getCardGroups(@QueryMap Map<String, Object> map);

    /**
     * 他人名片 - 最新趣晒
     */
    @GET("/app/interest/latestTalk?v=2.0.0")
    Observable<BaseJson<BaseListWrap<FunshowHome>>> getNewFunshowByUser(@Query("queryUserId") int queryUserId);

    /**
     * 他人名片 - 最近参与的话题
     */
    @GET("/app/topic/latestTopic?v=2.0.0")
    Observable<BaseJson<TopicHomeDetail>> getNewTopicByUser(@Query("queryUserId") int queryUserId);

//    /**
//     * 阅读趣点数量统计
//     */
//    @FormUrlEncoded
//    @POST("/app/news/addRealTotal?v=2.0.0")
//    Observable<BaseJson<Object>> readFunpoint(@Field("newsId") int newsId);

}
