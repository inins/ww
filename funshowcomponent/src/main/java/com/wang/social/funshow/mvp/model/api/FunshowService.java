package com.wang.social.funshow.mvp.model.api;

import com.frame.component.entities.BaseListWrap;
import com.frame.http.api.BaseJson;
import com.wang.social.funshow.mvp.entities.eva.Comment;
import com.wang.social.funshow.mvp.entities.eva.CommentReply;
import com.wang.social.funshow.mvp.entities.funshow.Funshow;
import com.wang.social.funshow.mvp.entities.funshow.FunshowDetail;
import com.wang.social.funshow.mvp.entities.post.FunshowAddPost;
import com.wang.social.funshow.mvp.entities.user.Friend;
import com.wang.social.funshow.mvp.entities.user.TopUser;
import com.wang.social.funshow.mvp.entities.user.ZanUser;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 */

public interface FunshowService {

    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

    /**
     * 趣晒列表
     * type 0 所有 1佬友
     */
    @GET("/app/talk/getTalkList?v=2.0.0")
    Observable<BaseJson<BaseListWrap<Funshow>>> getFunshowList(@Query("type") int type, @Query("current") int current, @Query("size") int size);

    /**
     * 趣晒详情
     */
    @GET("/app/talk/details?v=2.0.0")
    Observable<BaseJson<FunshowDetail>> getFunshowDetail(@Query("talkId") int talkId);

    /**
     * 趣晒魔列表
     * from 来源页面（square：广场；chat：聊天）
     */
    @GET("/app/talk/getReleaseTalkTopUser?v=2.0.0")
    Observable<BaseJson<BaseListWrap<TopUser>>> getFunshowTopUserList(@Query("from") String from, @Query("current") int current, @Query("size") int size);

    /**
     * 趣晒点赞
     * type类型1点赞 2取消点赞
     */
    @FormUrlEncoded
    @POST("/app/talk/talkSupport?v=2.0.0")
    Observable<BaseJson<Object>> funshowZan(@Field("talkId") int talkId, @Field("type") int type);

    /**
     * 趣晒点赞
     * type类型1点赞 2取消点赞
     */
    @FormUrlEncoded
    @POST("/app/talk/talkCommentSupport?v=2.0.0")
    Observable<BaseJson<Object>> funshowCommentZan(@Field("talkId") int talkId, @Field("talkCommentId") int talkCommentId, @Field("type") int type);

    /**
     * 趣晒点赞列表
     */
    @FormUrlEncoded
    @POST("/app/talk/supportList?v=2.0.0")
    Observable<BaseJson<BaseListWrap<ZanUser>>> funshowZanList(@Field("talkId") int talkId, @Field("current") int current, @Field("size") int size);

    /**
     * 趣晒评论列表
     */
    @GET("/app/talk/commentList?v=2.0.0")
    Observable<BaseJson<BaseListWrap<Comment>>> funshowEvaList(@Query("talkId") int talkId, @Query("current") int current, @Query("size") int size);

    /**
     * 发布趣晒评论，或者对评论进行回复
     */
    @Headers(HEADER_CONTENT_TYPE)
    @FormUrlEncoded
    @POST("/app/talk/talkComment?v=2.0.0")
    Observable<BaseJson<Object>> commentReply(@FieldMap Map<String, Object> param);

    /**
     * 好友列表
     */
    @GET("/app/userFriend/friendList?v=2.0.0")
    Observable<BaseJson<BaseListWrap<Friend>>> friendList();

    /**
     * 发布趣晒
     */
    @POST("/app/talk/saveTalk?v=2.0.0")
    Observable<BaseJson<Object>> addFunshow(@Body FunshowAddPost postBean);


}
