package com.wang.social.login.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.wang.social.login.mvp.model.entities.dto.TagsDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface TagService {

    /**
     *  获取一级标签列表
     * @param param
     * @return
     */
    @GET("app/tag/parentTagList")
    Observable<BaseJson<TagsDTO>> parentTagList(@QueryMap Map<String, Object> param);


    /**
     * 获取标签库列表
     * @param param
     * @return
     */
    @GET("app/tag/taglist")
    Observable<BaseJson<TagsDTO>> taglist(@QueryMap Map<String, Object> param);
}
