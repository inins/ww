package com.wang.social.im.mvp.model.api;

import com.frame.component.entities.FriendInfo;
import com.frame.component.entities.dto.FriendInfoDTO;
import com.frame.http.api.BaseJson;
import com.wang.social.im.mvp.model.entities.IndexFriendInfo;
import com.wang.social.im.mvp.model.entities.dto.IndexFriendInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;

import io.reactivex.Observable;
import retrofit2.http.GET;
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
     * @param version
     * @return
     */
    @GET("app/userFriend/friendList")
    Observable<BaseJson<ListDataDTO<IndexFriendInfoDTO, IndexFriendInfo>>> getFriendList(@Query("v") String version);
}
