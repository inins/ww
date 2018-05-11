package com.frame.component.ui.acticity.PersonalCard.model.api;

import com.frame.component.entities.dto.AccountBalanceDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.UserInfoDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.UserStatisticsDTO;
import com.frame.http.api.BaseJson;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface PersonalCardService {


    /**
     * 获取用户个人信息和相册
     */
    @FormUrlEncoded
    @POST("app/userInfo/getUserInfoAndPhotos")
    Observable<BaseJson<UserInfoDTO>> getUserInfoAndPhotos(@FieldMap Map<String, Object> param);

    /**
     * 用户数据统计（我的/推荐/个人名片）
     */
    @FormUrlEncoded
    @POST("app/userInfo/getUserStatistics")
    Observable<BaseJson<UserStatisticsDTO>> getUserStatistics(@FieldMap Map<String, Object> param);


}