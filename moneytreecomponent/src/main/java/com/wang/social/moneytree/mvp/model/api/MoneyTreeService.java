package com.wang.social.moneytree.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.wang.social.moneytree.mvp.model.entities.GameBeans;
import com.wang.social.moneytree.mvp.model.entities.dto.GameBeansDTO;
import com.wang.social.moneytree.mvp.model.entities.dto.GameRecordsDTO;
import com.wang.social.moneytree.mvp.model.entities.dto.NewGameDTO;

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
     * 创建游戏
     */
    @FormUrlEncoded
    @POST("app/moneyTree/createGame")
    Observable<BaseJson<NewGameDTO>> createGame(@FieldMap Map<String, Object> param);

    /**
     * 游戏列表
     */
    @GET("app/moneyTree/recordList")
    Observable<BaseJson<GameRecordsDTO>> getRecordList(@QueryMap Map<String, Object> param);
}
