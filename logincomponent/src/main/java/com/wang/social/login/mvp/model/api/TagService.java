package com.wang.social.login.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.wang.social.login.mvp.model.entities.dto.TagsDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface TagService {

    @GET("app/tag/parentTagList")
    Observable<BaseJson<TagsDTO>> parentTagList(@Query("v") String version);
}
