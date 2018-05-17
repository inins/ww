package com.wang.social.im.mvp.ui.PersonalCard.model.api;

import com.frame.component.entities.Topic;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;
import com.frame.component.entities.dto.GroupBeanDTO;
import com.frame.component.entities.dto.TopicDTO;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.DTO.UserInfoDTO;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.DTO.UserStatisticsDTO;
import com.frame.component.entities.GroupBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface PersonalCardService {
    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

    /**
     * 获取用户个人信息和相册
     */
    @FormUrlEncoded
    @POST("app/userInfo/getUserInfoAndPhotos")
    Observable<BaseJson<UserInfoDTO>> getUserInfoAndPhotos(@FieldMap Map<String, Object> param);

    /**
     * 用户数据统计（我的/推荐/个人名片）
     */
    @FormUrlEncoded
    @POST("app/userInfo/getUserStatistics")
    Observable<BaseJson<UserStatisticsDTO>> getUserStatistics(@FieldMap Map<String, Object> param);



    /**
     * 添加好友申请
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/userFriend/addApply")
    Observable<BaseJson> addFriendApply(@FieldMap Map<String, Object> param);

    /**
     * 同意、拒绝添加好友
     */
    @FormUrlEncoded
    @POST("app/userFriend/agreeOrRejectAdd")
    Observable<BaseJson> agreeOrRejectAdd(@FieldMap Map<String, Object> param);

    /**
     * 好友列表-好友设置备注
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/userFriend/setComment")
    Observable<BaseJson> setFriendComment(@FieldMap Map<String, Object> param);

    /**
     * 好友列表-好友设置头像
     */
    @FormUrlEncoded
    @POST("app/userFriend/setCommentAvatar")
    Observable<BaseJson> setFriendAvatar(@FieldMap Map<String, Object> param);

    /**
     * 删除好友关系
     */
    @FormUrlEncoded
    @POST("app/userFriend/delete")
    Observable<BaseJson> deleteFriend(@FieldMap Map<String, Object> param);

    /**
     * 拉黑/取消拉黑用户（我的/趣晒）
     */
    @FormUrlEncoded
    @POST("app/userInfo/changeMyBlack")
    Observable<BaseJson> changeMyBlack(@FieldMap Map<String, Object> param);



}