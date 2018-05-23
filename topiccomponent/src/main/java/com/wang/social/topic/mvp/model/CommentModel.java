package com.wang.social.topic.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.topic.mvp.contract.CommentContract;
import com.wang.social.topic.mvp.model.api.TopicService;
import com.wang.social.topic.mvp.model.entities.CommentRspDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class CommentModel extends BaseModel implements CommentContract.Model {

    @Inject
    public CommentModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }


    /**
     * 话题评论列表
     * @param topicId 话题ID
     * @Param commentId 评论ID
     * @param size 每页条数
     * @param current 当前页码
     * @return
     */
    @Override
    public Observable<BaseJson<CommentRspDTO>> commentList(int topicId, int commentId, int size, int current) {
        NetParam netParam = new NetParam();

        netParam.put("topicId", topicId);
        if (commentId != -1) {
            netParam.put("commentId", commentId);
        }
        netParam.put("size",size);
        netParam.put("current",current);
        netParam.put("v", "2.0.0");
        Map<String, Object> param = netParam.build();

        return mRepositoryManager
                .obtainRetrofitService(TopicService.class)
                .commentList(param);
    }

    /**
     * 评论话题/回复话题评论
     * @param topicId 话题ID
     * @param topicCommentId 话题评论ID：为空时表示评论话题
     * @param answeredUserId 被回复者ID
     * @param content 评论内容
     * @return
     */
    @Override
    public Observable<BaseJson> topicComment(int topicId, int topicCommentId, int answeredUserId, String content) {
//        Map<String, Object> param = new NetParam()
//                .put("topicId", topicId)
//                .put("topicCommentId", topicCommentId)
//                .put("answeredUserId",answeredUserId)
//                .put("content",content)
//                .put("v", "2.0.0")
//                .build();

        NetParam netParam = new NetParam();

        netParam.put("topicId", topicId);
        if (topicCommentId != -1) {
            netParam.put("topicCommentId", topicCommentId);
        } else {
            netParam.put("topicCommentId", "");
        }
        if (answeredUserId != -1) {
            netParam.put("answeredUserId", answeredUserId);
        } else {
            netParam.put("answeredUserId", "");
        }
        netParam.put("content",content);
        netParam.put("v", "2.0.0");
        Map<String, Object> param = netParam.build();

        return mRepositoryManager
                .obtainRetrofitService(TopicService.class)
                .topicComment(param);
    }

    /**
     * 话题评论点赞/取消评论点赞
     * @param topicId 话题ID
     * @param topicCommentId 话题评论ID
     * @param type 类型1点赞 2取消点赞
     * @return
     */
    @Override
    public Observable<BaseJson> topicCommentSupport(int topicId, int topicCommentId, String type) {
//        Map<String, Object> param = new NetParam()
//            .put("topicId", topicId)
//            .put("topicCommentId", topicCommentId)
//            .put("type",type)
//            .put("v", "2.0.0")
//            .build();

        NetParam netParam = new NetParam();

        netParam.put("topicId", topicId);
        if (topicCommentId != -1) {
            netParam.put("topicCommentId", topicCommentId);
        } else {
            netParam.put("topicCommentId", "");
        }
        netParam.put("type",type);
        netParam.put("v", "2.0.0");
        Map<String, Object> param = netParam.build();

        return mRepositoryManager
                .obtainRetrofitService(TopicService.class)
                .topicCommentSupport(param);
    }
}
