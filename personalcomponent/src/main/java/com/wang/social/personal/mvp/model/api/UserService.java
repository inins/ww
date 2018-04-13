package com.wang.social.personal.mvp.model.api;

import com.frame.http.api.BaseJson;
import com.wang.social.personal.mvp.base.BaseListWrap;
import com.wang.social.personal.mvp.entities.AccountBalance;
import com.wang.social.personal.mvp.entities.CommonEntity;
import com.wang.social.personal.mvp.entities.lable.Lable;
import com.wang.social.personal.mvp.entities.QiniuTokenWrap;
import com.wang.social.personal.mvp.entities.UserWrap;
import com.wang.social.personal.mvp.entities.lable.LableWrap;
import com.wang.social.personal.mvp.entities.photo.OffiPic;
import com.wang.social.personal.mvp.entities.photo.Photo;
import com.wang.social.personal.mvp.entities.photo.PhotoListWrap;
import com.wang.social.personal.mvp.entities.thirdlogin.BindHistory;
import com.wang.social.personal.mvp.entities.user.QrcodeInfo;

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

    //通过id 获取用户基本信息
    @GET("/user/getUserInfoByUserId?v=2.0.0")
    Observable<BaseJson<QrcodeInfo>> getUserInfoByUserId(@Query("userId") int userId);

    //个性标签
    @GET("/app/tag/showtag?v=2.0.0")
    Observable<BaseJson<LableWrap>> getShowtag();

    //添加个性标签
    @FormUrlEncoded
    @POST("/app/tag/update_showtag?v=2.0.0")
    Observable<BaseJson<Object>> addShowtag(@Field("tagIds") String tagIds);

    //个人标签
    @GET("/app/tag/selftags?v=2.0.0")
    Observable<BaseJson<LableWrap>> getSelftags(@Query("parentId") int parentId);

    //获取一级标签
    @GET("/app/tag/parentTagList?v=2.0.0")
    Observable<BaseJson<LableWrap>> getParentTags();

    //删除标签
    @FormUrlEncoded
    @POST("/app/tag/deltag?v=2.0.0")
    Observable<BaseJson<Object>> deltag(@Field("tagId") int tagId);

    //官方相册列表
    @GET("/system/pictureLibrary?v=2.0.0")
    Observable<BaseJson<BaseListWrap<OffiPic>>> getOfficialPhotoList(@Query("type") int type);

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

    //意见反馈
    @FormUrlEncoded
    @POST("/app/idea/feedback?v=2.0.0")
    Observable<BaseJson<Object>> feedback(@Field("phone") String phone, @Field("content") String content, @Field("pictures") String pictures);

    //绑定三方账户
    @FormUrlEncoded
    @POST("/app/userInfo/bind?v=2.0.0")
    Observable<BaseJson<Object>> bindThirdLogin(@Field("platform") String platform, @Field("uid") String uid);

    //解除绑定三方账户
    @FormUrlEncoded
    @POST("/app/userInfo/unBind?v=2.0.0")
    Observable<BaseJson<Object>> unBindThirdLogin(@Field("platform") String platform);

    //获取绑定记录
    @GET("/app/userInfo/bindList?v=2.0.0")
    Observable<BaseJson<BaseListWrap<BindHistory>>> bindList();

}
