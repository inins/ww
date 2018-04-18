package com.wang.social.topic.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.wang.social.topic.mvp.model.entities.dto.TagsDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicRspDTO;

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
}
