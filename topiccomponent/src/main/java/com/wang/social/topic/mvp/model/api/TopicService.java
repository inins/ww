package com.wang.social.topic.mvp.model.api;

import com.frame.component.entities.Topic;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;
import com.wang.social.topic.mvp.model.entities.CommentRspDTO;
import com.wang.social.topic.mvp.model.entities.dto.TemplatesDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicDetailDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicTopUsersDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface TopicService {

    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

    /**
     * 最新话题列表
     */
    @GET("app/topic/getNewsList")
    Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> getNewsList(@QueryMap Map<String, Object> param);

    /**
     * 最热话题列表
     */
    @GET("app/topic/getHotList")
    Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> getHotList(@QueryMap Map<String, Object> param);

    /**
     * 无人问津话题列表
     */
    @GET("app/topic/getLowList")
    Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> getLowList(@QueryMap Map<String, Object> param);


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
     * 话题评论列表
     */
    @GET("app/topic/commentList")
    Observable<BaseJson<CommentRspDTO>> commentList(@QueryMap Map<String, Object> param);


    /**
     * 评论话题/回复话题评论
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/topic/topicComment")
    Observable<BaseJson> topicComment(@FieldMap Map<String, Object> param);

    /**
     * 话题评论点赞/取消评论点赞
     */
    @FormUrlEncoded
    @POST("app/topic/topicCommentSupport")
    Observable<BaseJson> topicCommentSupport(@FieldMap Map<String, Object> param);

    /**
     * 话题点赞/取消点赞
     */
    @FormUrlEncoded
    @POST("app/topic/topicSupport")
    Observable<BaseJson> topicSupport(@FieldMap Map<String, Object> param);

    /**
     * 搜索话题
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/topic/searchTopic")
    Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> searchTopic(@FieldMap Map<String, Object> param);

    /**
     * 举报（用户/话题/趣聊/趣晒）
     */
    @FormUrlEncoded
    @POST("app/common/report")
    Observable<BaseJson> report(@FieldMap Map<String, Object> param);

    /**
     * 话题发布
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/topic/addTopic")
    Observable<BaseJson> addTopic(@FieldMap Map<String, Object> param);

    /**
     * 删除自己发布的话题
     */
    @FormUrlEncoded
    @POST("app/topic/delMyTopic")
    Observable<BaseJson> delMyTopic(@FieldMap Map<String, Object> param);
}
