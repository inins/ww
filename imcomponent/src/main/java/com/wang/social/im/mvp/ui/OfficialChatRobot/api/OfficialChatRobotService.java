package com.wang.social.im.mvp.ui.OfficialChatRobot.api;

import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;
import com.wang.social.im.mvp.ui.OfficialChatRobot.entities.LeavingMsg;
import com.wang.social.im.mvp.ui.OfficialChatRobot.entities.dto.LeavingMsgDTO;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.DTO.UserInfoDTO;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.DTO.UserStatisticsDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface OfficialChatRobotService {
    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

    /**
     * 新增留言
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/user_leaving_msg/save")
    Observable<BaseJson> userLeavingMsgSave(@FieldMap Map<String, Object> param);



    /**
     * 留言列表
     */
    @GET("app/user_leaving_msg/list")
    Observable<BaseJson<PageListDTO<LeavingMsgDTO, LeavingMsg>>> userLeavingMsgList(@QueryMap Map<String, Object> param);



}