package com.frame.component.api;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.GroupBean;
import com.frame.component.entities.dto.GroupMemberInfoDTO;
import com.frame.component.entities.dto.IsShoppingRspDTO;
import com.frame.component.entities.dto.MyTalkBeanDTO;
import com.frame.component.entities.dto.NewMoneyTreeGameDTO;
import com.frame.component.entities.PersonalInfo;
import com.frame.component.entities.Topic;
import com.frame.component.entities.UserWrap;
import com.frame.component.entities.dto.AccountBalanceDTO;
import com.frame.component.entities.dto.AddGroupApplyRspDTO;
import com.frame.component.entities.dto.AddGroupRspDTO;
import com.frame.component.entities.dto.GroupBeanDTO;
import com.frame.component.entities.dto.PersonalInfoDTO;
import com.frame.component.entities.dto.QiNiuDTO;
import com.frame.component.entities.dto.SearchUserInfoDTO;
import com.frame.component.entities.dto.SettingInfoDTO;
import com.frame.component.entities.dto.VersionInfoDTO;
import com.frame.component.entities.funshow.FunshowGroup;
import com.frame.component.entities.funshow.FunshowMe;
import com.frame.component.entities.topic.TopicGroup;
import com.frame.component.entities.topic.TopicMe;
import com.frame.component.entities.dto.TalkBeanDTO;
import com.frame.component.entities.dto.TopicDTO;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.entities.user.UserBoard;
import com.frame.component.ui.acticity.wwfriendsearch.entities.dto.SearchResultDTO;
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
     * 拉黑/取消拉黑用户（我的/趣晒）
     * shieldUserId  用户ID（多个ID，以逗号分隔）
     * type  类型 1:屏蔽拉黑 2：取消屏蔽
     */
    @FormUrlEncoded
    @POST("/app/userInfo/changeMyBlack?v=2.0.0")
    Observable<BaseJson<Object>> blankUser(@Field("blackUserIds") String shieldUserId, @Field("type") int type);

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

    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("/app/common/report?v=2.0.0")
    Observable<BaseJson<Object>> report(@Field("objectId") int objectId, @Field("type") int type,
                                        @Field("comment") String comment);

    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("/app/common/report?v=2.0.0")
    Observable<BaseJson<Object>> report(@Field("objectId") int objectId, @Field("type") int type, @Field("comment") String comment, @Field("picUrl") String picUrl);

    /**
     * 分享趣晒/话题
     * type 分享类型（topic:话题；group：趣聊；talk:趣晒; ）
     * shareType:
     */
    @FormUrlEncoded
    @POST("/app/share/save?v=2.0.0")
    Observable<BaseJson<Object>> sharefun(@Field("shareUserId") int shareUserId,
                                          @Field("targetUserId") Integer targetUserId,
                                          @Field("objectId") int objectId,
                                          @Field("type") String type,
                                          @Field("shareType") int shareType);


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
    Observable<BaseJson<UserBoard>> getUserInfoAndPhotos(@Field("userId") Integer userId);

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
    Observable<BaseJson> agreeFindChatApply(@Field("groupId") int groupId, @Field("otherUserId") int otherUserId, @Field("msgId") int msgId, @Field("type") int type);

    /**
     * 同意、拒绝邀请加入趣聊、觅聊（别人邀请我的）
     * groupId:趣聊/觅聊群id
     * msgId:消息id
     * type:类型（0：同意，1：拒绝）
     */
    @FormUrlEncoded
    @POST("app/group/agreeOrRejectAdd?v=2.0.0")
    Observable<BaseJson<AddGroupApplyRspDTO>> agreeGroupApply(@Field("groupId") int groupId, @Field("msgId") int msgId, @Field("type") int type);


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
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/group/addGroupMember")
    Observable<BaseJson<AddGroupRspDTO>> addGroupMember(@FieldMap Map<String, Object> param);

    //用户趣晒列表
    @FormUrlEncoded
    @POST("/app/talk/getMyTalkList?v=2.0.0")
    Observable<BaseJson<BaseListWrap<FunshowMe>>> getFunshowList(@Field("current") int current, @Field("size") int size);

    //用户话题列表
    @FormUrlEncoded
    @POST("/app/topic/getMyTopicList?v=2.0.0")
    Observable<BaseJson<BaseListWrap<TopicMe>>> getTopicList(@Field("current") int current, @Field("size") int size);

    //群成员趣晒列表
    @FormUrlEncoded
    @POST("/app/talk/getGroupTalkList?v=2.0.0")
    Observable<BaseJson<BaseListWrap<FunshowGroup>>> getGroupFunshowList(@Field("groupId") int groupId, @Field("current") int current, @Field("size") int size);

    //群成员话题列表
    @FormUrlEncoded
    @POST("/app/topic/getGroupTopicList?v=2.0.0")
    Observable<BaseJson<BaseListWrap<TopicGroup>>> getGroupTopicList(@Field("groupId") int groupId, @Field("current") int current, @Field("size") int size);


    /**
     * 趣晒列表 （他人名片）
     */
    @GET("app/talk/personalCardList")
    Observable<BaseJson<PageListDTO<TalkBeanDTO, FunshowBean>>>
    getFriendTalkList(@QueryMap Map<String, Object> param);


    /**
     * 用户趣晒列表-我的个人名片
     */
    @POST("app/talk/getMyTalkList") Observable<BaseJson<PageListDTO<MyTalkBeanDTO, FunshowBean>>>
        getMyTalkList(@QueryMap Map<String, Object> param);

    /**
     * 好友列表-他人名片查看
     */
    @GET("app/userFriend/othersFriendList")
    Observable<BaseJson<PageListDTO<PersonalInfoDTO, PersonalInfo>>>
    getUserFriendList(@QueryMap Map<String, Object> param);


    /**
     * 聊天列表-搜索已添加的好友
     */
    @Headers(HEADER_CONTENT_TYPE)
    @GET("app/chatList/searchUserList")
    Observable<BaseJson<PageListDTO<SearchUserInfoDTO, PersonalInfo>>>
    chatListSearchUser(@QueryMap Map<String, Object> param);

    /**
     * 聊天列表-搜索已加入的趣聊
     */
    @Headers(HEADER_CONTENT_TYPE)
    @GET("app/chatList/searchGroupList")
    Observable<BaseJson<PageListDTO<GroupBeanDTO, GroupBean>>>
    chatListSearchGroup(@QueryMap Map<String, Object> param);

    /**
     * 聊天列表-搜索已加入的觅聊
     */
    @Headers(HEADER_CONTENT_TYPE)
    @GET("app/chatList/searchMiList")
    Observable<BaseJson<PageListDTO<GroupBeanDTO, GroupBean>>>
    chatListSearchMiList(@QueryMap Map<String, Object> param);


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
     * 用户话题列表-我的个人名片
     */
    @GET("app/topic/getMyTopicList")
    Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> getMyTopicList(@QueryMap Map<String, Object> param);

    /**
     * 搜索话题
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/topic/searchTopic")
    Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> searchTopic(@FieldMap Map<String, Object> param);

    /**
     * 搜索趣聊
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/group/searchGroup")
    Observable<BaseJson<PageListDTO<GroupBeanDTO, GroupBean>>> searchGroup(@FieldMap Map<String, Object> param);

    /**
     * 搜索用户
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/userInfo/searchUser")
    Observable<BaseJson<PageListDTO<SearchUserInfoDTO, PersonalInfo>>> searchUser(@FieldMap Map<String, Object> param);


    /**
     * 创建游戏
     */
    @FormUrlEncoded
    @POST("app/moneyTree/createGame")
    Observable<BaseJson<NewMoneyTreeGameDTO>> createGame(@FieldMap Map<String, Object> param);


    /**
     * 游戏支付
     */
    @FormUrlEncoded
    @POST("app/moneyTree/pay")
    Observable<BaseJson> gamePay(@FieldMap Map<String, Object> param);


    /**
     * 2.2.8获取我在趣聊、觅聊群名片信息 判断用户是否在某群
     */
    @FormUrlEncoded
    @POST("app/group/getMyGroupMemberInfo")
    Observable<BaseJson<GroupMemberInfoDTO>> getMyGroupMemberInfo(@FieldMap Map<String, Object> param);

    /**
     * 系统消息已读
     */
    @FormUrlEncoded
    @POST("app/msg/readSysMsg?v=2.0.0")
    Observable<BaseJson> readSysMsg(@Field("type") int type);

    /**
     * 动态消息已读
     */
    @FormUrlEncoded
    @POST("app/msg/readDynMsg?v=2.0.0")
    Observable<BaseJson> readDynMsg(@Field("type") int type);

    /**
     * 动态消息已读
     */
    @Headers(HEADER_CONTENT_TYPE)
    @GET("app/share/listFriendAndGroup")
    Observable<BaseJson<SearchResultDTO>> listFriendAndGroup(@QueryMap Map<String, Object> param);

    /**
     * 动态消息已读
     */
    @GET("/app/talk/isShopping")
    Observable<BaseJson<IsShoppingRspDTO>> talkOrTopicIsShopping(@QueryMap Map<String, Object> param);

    /**
     * 获取用户设置信息
     *
     * @param version
     * @return
     */
    @GET("app/userInfo/setting")
    Observable<BaseJson<SettingInfoDTO>> getUserSetting(@Query("v") String version);

    /**
     * 版本更新检测
     *
     * @return
     */
    @GET("version/newest?v=2.0.0&platform=android")
    Observable<BaseJson<VersionInfoDTO>> checkNewVersion();
}