package com.wang.social.im.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.SimpleGroupInfo;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.model.entities.dto.CreateGroupResultDTO;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.MemberInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.PayCheckInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.SimpleGroupInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.SocialDTO;
import com.wang.social.im.mvp.model.entities.dto.SocialHomeDTO;
import com.wang.social.im.mvp.model.entities.dto.TeamInfoDTO;

import java.util.Map;

import io.reactivex.Observable;
import lombok.Getter;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * ============================================
 * 趣聊/觅聊相关接口
 * <p>
 * Create by ChenJing on 2018-05-02 9:27
 * ============================================
 */
public interface GroupService {

    /**
     * 查询觅聊列表类型：全部
     */
    int TEAM_LIST_ALL = 0;
    /**
     * 查询觅聊列表类型：我创建
     */
    int TEAM_LIST_CREATE = 1;
    /**
     * 查询觅聊列表类型：成员创建
     */
    int TEAM_LIST_MEMBER_CREATE = 2;

    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

    /**
     * 获取趣聊详情
     *
     * @param version
     * @param socialId
     * @return
     */
    @GET("app/group/getGroupInfo")
    Observable<BaseJson<SocialDTO>> getSocialInfo(@Query("v") String version, @Query("groupId") String socialId);

    /**
     * 趣聊主页获取趣聊详情
     *
     * @param version
     * @param socialId
     * @return
     */
    @GET("app/group/getGroupCombinationInfo")
    Observable<BaseJson<SocialHomeDTO>> getSocialHomeInfo(@Query("v") String version, @Query("groupId") String socialId);

    /**
     * 获取趣聊/觅聊成员列表
     *
     * @param version
     * @param groupId
     * @return
     */
    @GET("app/group/getGroupMemberList")
    Observable<BaseJson<ListDataDTO<MemberInfoDTO, MemberInfo>>> getMembers(@Query("v") String version, @Query("groupId") String groupId);

    /**
     * 获取觅聊详情
     *
     * @param version
     * @param teamId
     * @return
     */
    @GET("app/group/getMiGroupInfo")
    Observable<BaseJson<TeamInfoDTO>> getTeamInfo(@Query("v") String version, @Query("groupId") String teamId);

    /**
     * 检查修改分身信息是否需要支付费用
     *
     * @param version
     * @param groupId
     * @return
     */
    @POST("app/shadow/createUserShadowApply")
    Observable<BaseJson<PayCheckInfoDTO>> checkShadowStatus(@Query("v") String version, @Query("groupId") String groupId);

    /**
     * 修改分身信息
     *
     * @param version
     * @param socialId
     * @param orderId
     * @param nickname
     * @param portrait
     * @param gender   0女1男2迷
     * @return
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/shadow/updateUserShadow")
    Observable<BaseJson> updateShadowInfo(@Field("v") String version, @Field("groupId") String socialId, @Field("applyId") String orderId,
                                          @Field("nickname") String nickname, @Field("avatar") String portrait, @Field("sex") int gender);

    /**
     * 检查创建趣聊是否需要付费
     *
     * @param version
     * @return
     */
    @POST("app/group/createGroupApply")
    Observable<BaseJson<PayCheckInfoDTO>> checkCreateSocialStatus(@Query("v") String version);

    /**
     * 创建趣聊
     * "groupName":"标题",                     //趣聊群名称
     * “groupDesc”:”群介绍”,					  //群介绍
     * "groupCoverPlan":"http://xx.jpg",      //封面URL
     * "headUrl":"http://head.jpg",           //群头像URL
     * "isOpen":1,                            //是否公开: 0.封闭; 1.公开
     * "isFree":0,                            //是否免费: 0.收费; 1.免费用
     * "gemstone":100,                        //收费宝石个数
     * "gender":null,                         //性别限制 （null表示男女都可以） 0男 1女
     * "ageRange":"90,00",                    //年代区间
     * "isCreateMi":1,                        //是否允许创建觅1：允许创建2：不允许创建
     * "tagIds":"1,2"                         //标签ID，以,分隔
     *
     * @param map
     * @return
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/group/createGroup")
    Observable<BaseJson<CreateGroupResultDTO>> createSocial(@FieldMap Map<String, Object> map);

    /**
     * 检查创建觅聊是否需要付费
     *
     * @param version
     * @param socialId
     * @return
     */
    @POST("app/group/createMiGroupApply")
    Observable<BaseJson<PayCheckInfoDTO>> checkCreateTeamStatus(@Query("v") String version, @Query("groupId") String socialId);

    /**
     * 创建觅聊
     * "applyId":1,            //	订单申请ID
     * "groupName":"名称",          //	觅聊群名称
     * "groupCoverPlan":"http://fds",     //		封面URL
     * "headUrl":"",            //	群头像URL
     * “isOpen”,1				//是否开放加入群
     * "validation":0,         //	是否验证（0（默认） 不需要 1需要）
     * "isFree":0,             //是否免费: 0.收费; 1.免费用
     * "gemstone":20,           //	收费宝石个数
     * "tagIds":1           //标签ID
     *
     * @param map
     * @return
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/group/createMiGroup")
    Observable<BaseJson<CreateGroupResultDTO>> createTeam(@FieldMap Map<String, Object> map);

    /**
     * 修改趣聊/觅聊名片信息
     *
     * @param version
     * @param groupId
     * @param nickname
     * @param portrait
     * @param msgNotifyType 0全部 1不提示 2只提示＠我的
     * @return
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/group/updateMyGroupMemberInfo")
    Observable<BaseJson> updateNameCard(@Field("v") String version, @Field("groupId") String groupId,
                                        @Field("memberName") String nickname, @Field("memberHeadUrl") String portrait,
                                        @Field("receiveMsgType") int msgNotifyType);

    /**
     * 修改趣聊信息
     * "groupName":"标题",                    //趣聊群名称
     * “groupDesc”:”群介绍”,					 //群介绍
     * "groupCoverPlan":"http://xx.jpg",      //封面URL
     * "headUrl":"http://head.jpg",           //群头像URL
     * "isOpen":1,                            //是否公开: 0.封闭; 1.公开
     * "isFree":0,                            //是否免费: 0.收费; 1.免费用
     * "totalDiamond":100,                    //收费钻石个数
     * "gender":null,                         //性别限制 （null表示男女都可以） 0男 1女
     * "ageRange":"90,00",                    //年代区间
     * "isCreateMi":1,                        //是否允许创建觅1：允许创建2：不允许创建
     * "tagIds":"1,2"                         //标签ID，以,分隔
     *
     * @param map
     * @return
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/group/updateGroupInfo")
    Observable<BaseJson> updateSocialInfo(@FieldMap Map<String, Object> map);

    /**
     * 获取趣聊列表
     *
     * @param version
     * @param socialId
     * @param type     0：全部<默认>，1：我创建，2：成员创建
     * @return
     */
    @GET("app/group/getMiList")
    Observable<BaseJson<ListDataDTO<TeamInfoDTO, TeamInfo>>> getTeamList(@Query("v") String version, @Query("groupId") String socialId,
                                                                         @Query("type") int type);

    /**
     * 解散趣聊/觅聊
     *
     * @param version
     * @param groupId
     * @return
     */
    @POST("app/group/dissolutionGroup")
    Observable<BaseJson> dissolveGroup(@Query("v") String version, @Query("groupId") String groupId);

    /**
     * 退出趣聊/觅聊
     *
     * @param version
     * @param groupId
     * @return
     */
    @POST("app/group/userExitGroup")
    Observable<BaseJson> exitGroup(@Query("v") String version, @Query("groupId") String groupId);

    /**
     * 获取我的趣聊
     *
     * @param version
     * @return
     */
    @GET("app/group/joinList")
    Observable<BaseJson<ListDataDTO<SimpleGroupInfoDTO, SimpleGroupInfo>>> getBeinGroups(@Query("v") String version);
}