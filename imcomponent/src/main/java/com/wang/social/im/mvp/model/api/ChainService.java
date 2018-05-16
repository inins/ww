package com.wang.social.im.mvp.model.api;

import com.frame.component.entities.UserInfo;
import com.frame.component.entities.dto.UserInfoDTO;
import com.frame.http.api.BaseJson;
import com.wang.social.im.mvp.model.entities.IndexFriendInfo;
import com.wang.social.im.mvp.model.entities.dto.IndexFriendInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * ============================================
 * 关系链相关接口
 * <p>
 * Create by ChenJing on 2018-05-08 10:08
 * ============================================
 */
public interface ChainService {

    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

    /**
     * 获取好友列表
     *
     * @param version
     * @return
     */
    @GET("app/userFriend/friendList")
    Observable<BaseJson<ListDataDTO<IndexFriendInfoDTO, IndexFriendInfo>>> getFriendList(@Query("v") String version);

    //这个接口多处工程调用，已经转移到CommonService
//    /**
//     * 发送好友申请
//     *
//     * @param version
//     * @param userId
//     * @param reason
//     * @return
//     */
//    @Headers(HEADER_CONTENT_TYPE)
//    @FormUrlEncoded
//    @POST("app/userFriend/addApply")
//    Observable<BaseJson> sendFriendlyApply(@Field("v") String version, @Field("addUserId") String userId, @Field("reason") String reason);

    /**
     * 新用户
     *
     * @param version
     * @return
     */
    @GET("app/chatList/newUserList")
    Observable<BaseJson<ListDataDTO<UserInfoDTO, UserInfo>>> getNewUsers(@Query("v") String version);
}