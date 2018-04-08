package com.wang.social.login.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.wang.social.login.mvp.model.entities.dto.Tags;
import com.wang.social.login.mvp.model.entities.dto.TagsDTO;

import io.reactivex.Observable;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface TagService {


    @GET("app/tag/parentTagList")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Observable<BaseJson<TagsDTO>> parentTagList();
}
