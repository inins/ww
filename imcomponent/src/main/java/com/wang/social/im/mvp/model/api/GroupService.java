package com.wang.social.im.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.SocialInfo;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.MemberInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.SocialDTO;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * ============================================
 * 趣聊/觅聊相关接口
 * <p>
 * Create by ChenJing on 2018-05-02 9:27
 * ============================================
 */
public interface GroupService {

    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

    /**
     * 获取趣聊详情
     * @param version
     * @param socialId
     * @return
     */
    @GET("app/group/getGroupInfo")
    Observable<BaseJson<SocialDTO>> getSocialInfo(@Query("v") String version, @Query("groupId") String socialId);

    /**
     * 获取趣聊/觅聊成员列表
     * @param version
     * @param groupId
     * @return
     */
    @GET("app/group/getGroupMemberList")
    Observable<BaseJson<ListDataDTO<MemberInfoDTO, MemberInfo>>> getMembers(@Query("v") String version, @Query("groupId") String groupId);
}