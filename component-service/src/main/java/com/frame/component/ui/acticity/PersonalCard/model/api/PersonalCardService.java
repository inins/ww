package com.frame.component.ui.acticity.PersonalCard.model.api;

import com.frame.component.entities.Topic;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.FriendListDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.TalkBeanListDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.TopicDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.UserInfoDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.UserStatisticsDTO;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface PersonalCardService {


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
     * 好友列表-他人名片查看
     */
    @GET("app/userFriend/othersFriendList")
    Observable<BaseJson<FriendListDTO>> getUserFriendList(@QueryMap Map<String, Object> param);

    /**
     * 趣聊列表-他人名片
     */
    @GET("app/group/list")
    Observable<BaseJson<TalkBeanListDTO>> getGroupList(@QueryMap Map<String, Object> param);


    /**
     * 话题列表 （他人名片）
     */
    @GET("app/topic/personalCardList")
    Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> getFriendTopicList(@QueryMap Map<String, Object> param);
}