package com.wang.social.topic.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.wang.social.topic.mvp.model.entities.dto.TagsDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TagService {

    /**
     * 已选推荐标签列表（首页/话题）
     */
    @FormUrlEncoded
    @POST("app/tag/myRecommendTag")
    Observable<BaseJson<TagsDTO>> myRecommendTag(@FieldMap Map<String, Object> param);
}
