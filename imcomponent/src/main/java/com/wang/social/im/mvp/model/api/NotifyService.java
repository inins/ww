package com.wang.social.im.mvp.model.api;

import com.frame.component.entities.BaseListWrap;
import com.frame.http.api.BaseJson;
import com.wang.social.im.mvp.model.entities.notify.AiteMsg;
import com.wang.social.im.mvp.model.entities.notify.EvaMsg;
import com.wang.social.im.mvp.model.entities.notify.FriendRequest;
import com.wang.social.im.mvp.model.entities.notify.GroupRequest;
import com.wang.social.im.mvp.model.entities.notify.FindChatRequest;
import com.wang.social.im.mvp.model.entities.notify.SysMsg;
import com.wang.social.im.mvp.model.entities.notify.ZanMsg;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-14 17:43
 * ============================================
 */
public interface NotifyService {

    /**
     * 系统消息
     */
    @GET("/app/msg/sysMsg?v=2.0.0")
    Observable<BaseJson<BaseListWrap<SysMsg>>> getSysMsgList(@Query("current") int current, @Query("size") int size);

    /**
     * 好友申请列表
     */
    @GET("/app/msg/friendApply?v=2.0.0")
    Observable<BaseJson<BaseListWrap<FriendRequest>>> getFriendRequestList(@Query("current") int current, @Query("size") int size);

    /**
     * 加入觅聊申请消息
     */
    @GET("/app/msg/joinGroupApply?v=2.0.0")
    Observable<BaseJson<BaseListWrap<FindChatRequest>>> getGroupRequstList(@Query("current") int current, @Query("size") int size);

    /**
     * 加群邀请
     */
    @GET("/app/msg/joinGroupInvite?v=2.0.0")
    Observable<BaseJson<BaseListWrap<GroupRequest>>> getGroupJoinRequstList(@Query("current") int current, @Query("size") int size);

    /**
     * 点赞消息列表
     */
    @GET("/app/msg/modeLike?v=2.0.0")
    Observable<BaseJson<BaseListWrap<ZanMsg>>> getZanMsgList(@Query("current") int current, @Query("size") int size);

    /**
     * 评论消息列表
     */
    @GET("/app/msg/modeComment?v=2.0.0")
    Observable<BaseJson<BaseListWrap<EvaMsg>>> getEvaMsgList(@Query("current") int current, @Query("size") int size);

    /**
     * @消息列表
     */
    @GET("/app/msg/modeWhitMe?v=2.0.0")
    Observable<BaseJson<BaseListWrap<AiteMsg>>> getAiteMsgList(@Query("current") int current, @Query("size") int size);

}
