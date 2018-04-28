package com.wang.social.im.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;
import com.wang.social.im.mvp.model.entities.EnvelopAdoptInfo;
import com.wang.social.im.mvp.model.entities.dto.CreateEnvelopResultDTO;
import com.wang.social.im.mvp.model.entities.dto.EnvelopAdoptInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.EnvelopInfoDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * ============================================
 * 红包相关接口
 * <p>
 * Create by ChenJing on 2018-04-27 9:29
 * ============================================
 */
public interface EnvelopService {

    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

    /**
     * 发送红包
     * <p>
     * version
     * versionCode
     * channelCode
     * diamond
     * envelopType  0: 一对一红包  1：普通红包 2： 拼手气红包
     * envelopCount
     * message
     * groupId
     * nonceStr
     * signature
     *
     * @return
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("app/red/create")
    Observable<BaseJson<CreateEnvelopResultDTO>> createEnvelop(@FieldMap Map<String, Object> map);

    /**
     * 领取红包
     *
     * @param version
     * @param envelopId
     * @return
     */
    @POST("app/red/receive")
    Observable<BaseJson<EnvelopAdoptInfoDTO>> adoptEnvelop(@Query("v") String version, @Query("packId") long envelopId);

    /**
     * 红包详情
     *
     * @param version
     * @param envelopId
     * @return
     */
    @GET("app/red/details")
    Observable<BaseJson<EnvelopInfoDTO>> getEnvelopInfo(@Query("v") String version, @Query("packId") long envelopId);

    /**
     * 红包领取记录
     * @param version
     * @param envelopId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GET("app/red/receive_record")
    Observable<BaseJson<PageListDTO<EnvelopAdoptInfoDTO, EnvelopAdoptInfo>>> getEnvelopAdoptList(@Query("v") String version, @Query("packId") long envelopId,
                                                                                                 @Query("current") int currentPage, @Query("size") int pageSize);
}