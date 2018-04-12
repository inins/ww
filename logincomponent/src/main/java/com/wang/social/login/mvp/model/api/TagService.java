package com.wang.social.login.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.wang.social.login.mvp.model.entities.dto.TagsDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface TagService {

    /**
     *  获取一级标签列表
     */
    @GET("app/tag/parentTagList")
    Observable<BaseJson<TagsDTO>> parentTagList(@QueryMap Map<String, Object> param);


    /**
     * 获取标签库列表
     */
    @GET("app/tag/taglist")
    Observable<BaseJson<TagsDTO>> taglist(@QueryMap Map<String, Object> param);


    /**
     * 编辑推荐标签（首页/话题）
     */
    @FormUrlEncoded
    @POST("app/tag/updateRecommendTag")
    Observable<BaseJson> updateRecommendTag(@FieldMap Map<String, Object> param);


    /**
     * 已选推荐标签列表（首页/话题）
     */
    @FormUrlEncoded
    @POST("app/tag/myRecommendTag")
    Observable<BaseJson<TagsDTO>> myRecommendTag(@FieldMap Map<String, Object> param);

    /**
     * 添加个人标签
     */
    @FormUrlEncoded
    @POST("app/tag/add")
    Observable<BaseJson> addPersonalTag(@FieldMap Map<String, Object> param);
}
