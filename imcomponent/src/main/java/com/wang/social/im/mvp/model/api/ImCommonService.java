package com.wang.social.im.mvp.model.api;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.User;
import com.frame.component.entities.UserInfo;
import com.frame.component.entities.dto.UserInfoDTO;
import com.frame.http.api.BaseJson;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-16 9:47
 * ============================================
 */
public interface ImCommonService {

    /**
     * 获取知识魔
     *
     * @param param
     * @return
     */
    @GET("app/topic/getReleaseTopicTopUser")
    Observable<BaseJson<ListDataDTO<UserInfoDTO, UserInfo>>> getKnowledgeUser(@QueryMap Map<String, Object> param);

    /**
     * 趣晒魔列表
     * from 来源页面（square：广场；chat：聊天）
     */
    @GET("/app/talk/getReleaseTalkTopUser?v=2.0.0")
    Observable<BaseJson<ListDataDTO<UserInfoDTO, UserInfo>>> getFunShowUserList(@Query("from") String from, @Query("current") int current, @Query("size") int size);
}