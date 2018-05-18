package com.wang.social.moneytree.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.wang.social.moneytree.mvp.model.entities.dto.GameEndDTO;
import com.wang.social.moneytree.mvp.model.entities.dto.GameRecordDetailDTO;
import com.wang.social.moneytree.mvp.model.entities.MemberList;
import com.wang.social.moneytree.mvp.model.entities.dto.GameBeansDTO;
import com.wang.social.moneytree.mvp.model.entities.dto.GameRecordsDTO;
import com.wang.social.moneytree.mvp.model.entities.dto.JoinGameDTO;
import com.wang.social.moneytree.mvp.model.entities.dto.RoomMsgDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface MoneyTreeService {

    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

    /**
     * 游戏列表
     */
    @GET("app/moneyTree/list")
    Observable<BaseJson<GameBeansDTO>> getMoneyTreeList(@QueryMap Map<String, Object> param);

    /**
     * 游戏记录
     */
    @GET("app/moneyTree/recordList")
    Observable<BaseJson<GameRecordsDTO>> getRecordList(@QueryMap Map<String, Object> param);

    /**
     * 游戏记录详情
     */
    @GET("app/moneyTree/recordDetail")
    Observable<BaseJson<GameRecordDetailDTO>> getRecordDetail(@QueryMap Map<String, Object> param);

    /**
     * 获取游戏房间展示项
     */
    @GET("app/moneyTree/getRoomMsg")
    Observable<BaseJson<RoomMsgDTO>> getRoomMsg(@QueryMap Map<String, Object> param);

    /**
     * 加入游戏
     */
    @FormUrlEncoded
    @POST("app/moneyTree/joinGame")
    Observable<BaseJson<JoinGameDTO>> joinGame(@FieldMap Map<String, Object> param);

    /**
     * 游戏成员列表
     */
    @GET("app/moneyTree/memberList")
    Observable<BaseJson<MemberList>> getMemberList(@QueryMap Map<String, Object> param);

    /**
     * 游戏结果
     */
    @GET("app/moneyTree/gameEnd")
    Observable<BaseJson<GameEndDTO>> getGameEnd(@QueryMap Map<String, Object> param);
}
