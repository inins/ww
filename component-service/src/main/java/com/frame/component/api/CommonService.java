package com.frame.component.api;

import com.frame.component.entities.DiamondNum;
import com.frame.component.entities.UserWrap;
import com.frame.component.entities.dto.DiamondNumDTO;
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

    /**
     * 宝石兑换
     * price	Integer	宝石数量
     * objectType	String	exchange
     * payChannels	String	支付渠道:支付渠道-----支付宝(aliPay),微信(weixinPay)
     * nonceStr	String	随机数
     * versionCode	String	版本号
     * channelCode	String	渠道号
     * signature	String	参数签名
     */
    @FormUrlEncoded
    @POST("/app/userWallet/exchange")
    Observable<BaseJson<Object>> exchangeStone(@FieldMap Map<String, Object> param);

    //用户支付（趣晒/话题/创建趣聊/加入趣聊/创建觅聊支付/加入觅聊）
    @FormUrlEncoded
    @POST("/app/order/createOrder")
    Observable<BaseJson<Object>> payStone(@FieldMap Map<String, Object> param);

    /**
     * 举报（用户/话题/趣聊/趣晒）
     * type 举报类型（0人 1趣聊 2趣晒 3主播 4 话题）
     */
    @FormUrlEncoded
    @POST("/app/common/report?v=2.0.0")
    Observable<BaseJson<Object>> report(@Field("objectId") int objectId, @Field("type") int type);


    /**
     * 举报（用户/话题/趣聊/趣晒）
     * type 举报类型（0人 1趣聊 2趣晒 3主播 4 话题）
     */
    @FormUrlEncoded
    @POST("/app/userInfo/findMyWallet")
    Observable<BaseJson<DiamondNum>> findMyWallet(@FieldMap Map<String, Object> param);
}