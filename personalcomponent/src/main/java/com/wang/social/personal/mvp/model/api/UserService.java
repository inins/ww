package com.wang.social.personal.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.frame.http.api.BaseListJson;
import com.wang.social.personal.mvp.entities.AccountBalance;
import com.wang.social.personal.mvp.entities.CommonEntity;
import com.wang.social.personal.mvp.entities.Lable;
import com.wang.social.personal.mvp.entities.QiniuTokenWrap;
import com.wang.social.personal.mvp.entities.UserWrap;
import com.wang.social.personal.mvp.entities.photo.Photo;
import com.wang.social.personal.mvp.entities.photo.PhotoListWrap;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 */

public interface UserService {

    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

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
     * 获取七牛云上传token
     */
    @POST("/common/upToken")
    Observable<BaseJson<QiniuTokenWrap>> getQiniuToken();

    /**
     * 修改个人信息
     * nickname
     * avatar
     * sex
     * birthday
     * autograph
     * province
     * city
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("/app/userInfo/changeInfo?v=2.0.0")
    Observable<BaseJson<CommonEntity>> updateUserInfo(@FieldMap Map<String, Object> param);

    @GET("/app/userWallet/accountBalance?v=2.0.0")
    Observable<BaseJson<AccountBalance>> accountBalance();

    //个性标签
    @GET("/app/tag/showtag?v=2.0.0")
    Observable<BaseListJson<Lable>> getShowtag();

    //个人标签
    @GET("/app/tag/selftags?v=2.0.0")
    Observable<BaseListJson<Lable>> getSelftags();

    //个人相册列表
    @POST("/app/userInfo/getPhotoList?v=2.0.0")
    Observable<BaseJson<PhotoListWrap>> getPhotoList();

    //添加个人相册
    @FormUrlEncoded
    @POST("/app/userInfo/addPhoto?v=2.0.0")
    Observable<BaseJson<Photo>> addPhoto(@Field("photoUrl") String photoUrl);

    //删除个人相册
    @FormUrlEncoded
    @POST("/app/userInfo/delPhoto?v=2.0.0")
    Observable<BaseJson<Boolean>> delPhoto(@Field("userPhotoId") int userPhotoId);

    //修改个人相册
    @FormUrlEncoded
    @POST("/app/userInfo/editPhoto?v=2.0.0")
    Observable<BaseJson<Boolean>> editPhoto(@Field("userPhotoId") int userPhotoId, @Field("photoUrl") String photoUrl);
}
