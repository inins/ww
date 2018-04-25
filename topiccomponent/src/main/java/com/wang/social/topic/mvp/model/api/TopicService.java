package com.wang.social.topic.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.wang.social.topic.mvp.model.entities.dto.MusicsDTO;
import com.wang.social.topic.mvp.model.entities.dto.TemplatesDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicDetailDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicRspDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicTopUsersDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface TopicService {

    /**
     * 最新话题列表
     */
    @GET("app/topic/getNewsList")
    Observable<BaseJson<TopicRspDTO>> getNewsList(@QueryMap Map<String, Object> param);

    /**
     * 最热话题列表
     */
    @GET("app/topic/getHotList")
    Observable<BaseJson<TopicRspDTO>> getHotList(@QueryMap Map<String, Object> param);

    /**
     * 无人问津话题列表
     */
    @GET("app/topic/getLowList")
    Observable<BaseJson<TopicRspDTO>> getLowList(@QueryMap Map<String, Object> param);


    /**
     * 无人问津话题列表
     */
    @GET("app/topic/getReleaseTopicTopUser")
    Observable<BaseJson<TopicTopUsersDTO>> getReleaseTopicTopUser(@QueryMap Map<String, Object> param);

    /**
     * 话题详情
     */
    @FormUrlEncoded
    @POST("app/topic/getTopicDetails")
    Observable<BaseJson<TopicDetailDTO>> getTopicDetails(@FieldMap Map<String, Object> param);

    /**
     * 模板列表
     */
    @GET("app/common/templeList")
    Observable<BaseJson<TemplatesDTO>> templeList(@QueryMap Map<String, Object> param);


    /**
     * 歌曲列表（发布添加歌曲）
     */
    @GET("app/common/musicList")
    Observable<BaseJson<MusicsDTO>> musicList(@QueryMap Map<String, Object> param);


}
