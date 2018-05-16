package com.frame.component.api;

import com.frame.component.entities.ShareUserInfo;
import com.frame.component.entities.UserWrap;
import com.frame.component.entities.dto.AccountBalanceDTO;
import com.frame.component.entities.dto.AddGroupApplyRspDTO;
import com.frame.component.entities.dto.AddGroupRspDTO;
import com.frame.component.entities.dto.QiNiuDTO;
import com.frame.component.entities.dto.ShareUserInfoDTO;
import com.frame.component.entities.user.UserBoard;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-22 17:11
 * =========================================
 */

public interface CommonService {

    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

    /**
     * 获取七牛云上传token
     */
    @POST("/common/upToken")
    Observable<BaseJson<QiNiuDTO>> getQiNiuToken();

    /**
     * 增加/取消屏蔽用户(在多处使用)
     * shieldUserId  用户ID（多个ID，以逗号分隔）
     * type  类型 1:屏蔽拉黑 2：取消屏蔽
     */
    @FormUrlEncoded
    @POST("/app/userInfo/changeMyShield?v=2.0.0")
    Observable<BaseJson<Object>> shatDownUser(@Field("shieldUserId") String shieldUserId, @Field("type") int type);

    /**
     * 登陆
     * mobile
     * password
     * nonceStr
     * signature
     */
    @FormUrlEncoded
    @POST("/login/password")
    Observable<BaseJson<UserWrap>> login(@FieldMap Map<String, Object> param);

    /**
     * 宝石兑换
     * price	Integer	宝石数量
     * objectType	String	exchange
     * payChannels	String	支付渠道:支付渠道-----支付宝(aliPay),微信(weixinPay)
     * nonceStr	String	随机数
     * versionCode	String	版本号
     * channelCode	String	渠道号
     * signature	String	参数签名
     */
    @FormUrlEncoded
    @POST("/app/userWallet/exchange")
    Observable<BaseJson<Object>> exchangeStone(@FieldMap Map<String, Object> param);

    //用户支付（趣晒/话题/创建趣聊/加入趣聊/创建觅聊支付/加入觅聊）
    @FormUrlEncoded
    @POST("/app/order/createOrder")
    Observable<BaseJson<Object>> payStone(@FieldMap Map<String, Object> param);

    /**
     * 举报（用户/话题/趣聊/趣晒）
     * type 举报类型（0人 1趣聊 2趣晒 3主播 4 话题）
     */
    @FormUrlEncoded
    @POST("/app/common/report?v=2.0.0")
    Observable<BaseJson<Object>> report(@Field("objectId") int objectId, @Field("type") int type);
    @FormUrlEncoded
    @POST("/app/common/report?v=2.0.0")
    Observable<BaseJson<Object>> report(@Field("objectId") int objectId, @Field("type") int type,
                                        @Field("comment") String comment);

    /**
     * 分享趣晒/话题
     * type 分享类型（topic:话题；group：趣聊；talk:趣晒; ）
     */
    @FormUrlEncoded
    @POST("/app/share/save?v=2.0.0")
    Observable<BaseJson<Object>> sharefun(@Field("targetUserId") Integer targetUserId, @Field("objectId") int objectId, @Field("type") String type);


    /**
     * 获取用户自己的账户余额信息
     */

    @GET("/app/userWallet/accountBalance")
    Observable<BaseJson<AccountBalanceDTO>> accountBalance(@QueryMap Map<String, Object> param);

    /**
     * 趣晒点赞
     * type类型1点赞 2取消点赞
     */
    @FormUrlEncoded
    @POST("/app/talk/talkSupport?v=2.0.0")
    Observable<BaseJson<Object>> funshowZan(@Field("talkId") int talkId, @Field("type") int type);

    /**
     * 趣晒评论点赞
     * type类型1点赞 2取消点赞
     */
    @FormUrlEncoded
    @POST("/app/talk/talkCommentSupport?v=2.0.0")
    Observable<BaseJson<Object>> funshowCommentZan(@Field("talkId") int talkId, @Field("talkCommentId") int talkCommentId, @Field("type") int type);

    /**
     * 话题点赞
     * type类型1点赞 2取消点赞
     */
    @FormUrlEncoded
    @POST("/app/topic/topicSupport?v=2.0.0")
    Observable<BaseJson<Object>> topicZan(@Field("topicId") int topicId, @Field("type") int type);

    /**
     * 阅读趣点数量统计
     */
    @FormUrlEncoded
    @POST("/app/news/addRealTotal?v=2.0.0")
    Observable<BaseJson<Object>> readFunpoint(@Field("newsId") int newsId);

    /**
     * 获取用户信息包括相册
     */
    @FormUrlEncoded
    @POST("/app/userInfo/getUserInfoAndPhotos?v=2.0.0")
    Observable<BaseJson<UserBoard>> getUserInfoAndPhotos(@Field("userId") int userId);

    /**
     * 发送好友申请
     *
     * @param version
     * @param userId
     * @param reason
     * @return
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/userFriend/addApply")
    Observable<BaseJson> sendFriendlyApply(@Field("v") String version, @Field("addUserId") String userId, @Field("reason") String reason);

    /**
     * 同意、拒绝添加好友
     * 类型（0：同意，1：拒绝）
     */
    @FormUrlEncoded
    @POST("app/userFriend/agreeOrRejectAdd?v=2.0.0")
    Observable<BaseJson> agreeFriendApply(@Field("friendUserId") int friendUserId, @Field("msgId") int msgId, @Field("type") int type);

    /**
     * 同意、拒绝加入趣聊、觅聊申请 （别人申请加入我的趣聊/觅聊的）
     * groupId:趣聊/觅聊群id
     * otherUserId:申请人用户id
     * msgId:消息id
     * type:类型（0：同意，1：拒绝）
     */
    @FormUrlEncoded
    @POST("app/group/applyAddGroupOpera?v=2.0.0")
    Observable<BaseJson> agreeGroupApply(@Field("groupId") int groupId, @Field("otherUserId") int otherUserId, @Field("msgId") int msgId, @Field("type") int type);

    /**
     * 同意、拒绝邀请加入趣聊、觅聊（别人邀请我的）
     * groupId:趣聊/觅聊群id
     * msgId:消息id
     * type:类型（0：同意，1：拒绝）
     */
    @FormUrlEncoded
    @POST("app/group/agreeOrRejectAdd?v=2.0.0")
    Observable<BaseJson> agreeGroupJoinApply(@Field("groupId") int groupId, @Field("msgId") int msgId, @Field("type") int type);



    /**
     * 创建加入趣聊、觅聊申请
     */
    @FormUrlEncoded
    @POST("app/group/addGroupMemberApply")
    Observable<BaseJson<AddGroupApplyRspDTO>> addGroupMemberApply(
            @Field("v") String version, @Field("groupId") int groupId);

    /**
     * 尝试加入趣聊、觅聊
     */
    @FormUrlEncoded
    @POST("app/group/addGroupMember")
    Observable<BaseJson<AddGroupRspDTO>> addGroupMember(
            @Field("v") String version, @Field("applyId") int applyId);
}