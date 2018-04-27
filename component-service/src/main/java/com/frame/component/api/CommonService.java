package com.frame.component.api;

import com.frame.component.entities.UserWrap;
import com.frame.component.entities.dto.QiNiuDTO;
import com.frame.http.api.BaseJson;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-22 17:11
 * =========================================
 */

public interface CommonService {

    /**
     * 获取七牛云上传token
     */
    @POST("/common/upToken")
    Observable<BaseJson<QiNiuDTO>> getQiNiuToken();

    /**
     * 增加/取消屏蔽用户(在多处使用)
     * shieldUserId  用户ID（多个ID，以逗号分隔）
     * type  类型 1:屏蔽拉黑 2：取消屏蔽
     */
    @FormUrlEncoded
    @POST("/app/userInfo/changeMyShield?v=2.0.0")
    Observable<BaseJson<Object>> shatDownUser(@Field("shieldUserId") String shieldUserId, @Field("type") int type);

    /**
     * 登陆
     * mobile
     * password
     * nonceStr
     * signature
     */
    @FormUrlEncoded
    @POST("/login/password")
    Observable<BaseJson<UserWrap>> login(@FieldMap Map<String, Object> param);
}