package com.wang.social.login.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.wang.social.login.mvp.model.entities.dto.TagsDTO;

import io.reactivex.Observable;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TagService {

    @FormUrlEncoded
    @POST("app/tag/parentTagList")
    Observable<BaseJson<TagsDTO>> parentTagList();
}
