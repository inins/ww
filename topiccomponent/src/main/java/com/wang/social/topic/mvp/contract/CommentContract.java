package com.wang.social.topic.mvp.contract;

import android.content.Context;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.topic.mvp.model.entities.Comment;
import com.wang.social.topic.mvp.model.entities.dto.CommentsDTO;
import com.wang.social.topic.mvp.model.entities.dto.TemplatesDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;

public interface CommentContract {
    interface View extends IView {
        void showToastLong(String msg);
        void onLoadCommentSuccess();
        void onLoadCommentCompleted();
        void setReplyCount(int count);
        void onCommitCommentSuccess();
        void onSupportSuccess(Comment comment);
    }


    interface Model extends IModel {
        Observable<BaseJson<CommentsDTO>> commentList(int topicId, int commentId, int size, int current);
        Observable<BaseJson> topicComment(int topicId, int topicCommentId, int answeredUserId, String content);
        Observable<BaseJson> topicCommentSupport(int topicId, int topicCommentId, String type);
    }
}
