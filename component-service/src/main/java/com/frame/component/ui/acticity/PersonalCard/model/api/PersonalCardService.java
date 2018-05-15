package com.frame.component.ui.acticity.PersonalCard.model.api;

import com.frame.component.entities.Topic;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.FriendListDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.GroupBeanDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.SearchUserInfoDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.TalkBeanDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.TopicDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.UserInfoDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.UserStatisticsDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.GroupBean;
import com.frame.component.ui.acticity.PersonalCard.model.entities.PersonalInfo;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageList;
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
    Observable<BaseJson<PageListDTO<GroupBeanDTO, GroupBean>>> getGroupList(@QueryMap Map<String, Object> param);


    /**
     * 话题列表 （他人名片）
     */
    @GET("app/topic/personalCardList")
    Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> getFriendTopicList(@QueryMap Map<String, Object> param);

    /**
     * 趣晒列表 （他人名片）
     */
    @GET("app/talk/personalCardList")
    Observable<BaseJson<PageListDTO<TalkBeanDTO, FunshowBean>>> getFriendTalkList(@QueryMap Map<String, Object> param);


    /**
     * 添加好友申请
     */
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


    /**
     * 聊天列表-搜索已添加的好友
     */
    @FormUrlEncoded
    @POST("app/chatList/searchUserList")
    Observable<BaseJson<PageListDTO<SearchUserInfoDTO, PersonalInfo>>> chatListSearchUser(@FieldMap Map<String, Object> param);

    /**
     * 聊天列表-搜索已加入的趣聊
     */
    @FormUrlEncoded
    @POST("app/chatList/searchGroupList")
    Observable<BaseJson<PageListDTO<GroupBeanDTO, GroupBean>>> chatListSearchGroup(@FieldMap Map<String, Object> param);

    /**
     * 聊天列表-搜索已加入的觅聊
     */
    @FormUrlEncoded
    @POST("app/chatList/searchMiList")
    Observable<BaseJson<PageListDTO<GroupBeanDTO, GroupBean>>> chatListSearchMiList(@FieldMap Map<String, Object> param);



}