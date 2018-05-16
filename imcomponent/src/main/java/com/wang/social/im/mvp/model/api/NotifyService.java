package com.wang.social.im.mvp.model.api;

import com.frame.component.entities.BaseListWrap;
import com.frame.http.api.BaseJson;
import com.wang.social.im.mvp.model.entities.notify.FriendRequest;
import com.wang.social.im.mvp.model.entities.notify.GroupRequest;
import com.wang.social.im.mvp.model.entities.notify.SysMsg;

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
     * 加入趣聊申请消息
     */
    @GET("/app/msg/joinGroupApply?v=2.0.0")
    Observable<BaseJson<BaseListWrap<GroupRequest>>> getGroupRequstList(@Query("current") int current, @Query("size") int size);

}
