package com.wang.social.topic.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.CommentContract;
import com.wang.social.topic.mvp.model.entities.Comment;
import com.wang.social.topic.mvp.model.entities.CommentRsp;
import com.wang.social.topic.mvp.model.entities.Comments;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
@ActivityScope
public class CommentPresenter extends
        BasePresenter<CommentContract.Model, CommentContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    private int mCurrent = 0;
    private final static int mSize = 10;

    private List<Comment> mCommentList = new ArrayList<>();
    // 保存二级页面的主评论类容
    private Comment mComment;

    @Inject
    public CommentPresenter(CommentContract.Model model, CommentContract.View view) {
        super(model, view);
    }

    public List<Comment> getCommentList() {
        return mCommentList;
    }

    public void setComment(Comment comment) {
        mComment = comment;
    }

    /**
     * 评论话题/回复话题评论
     * @param topicId 话题ID
     * @param topicCommentId 话题评论ID：为空时表示评论话题
     * @param answeredUserId 被回复者ID
     * @param content 评论内容
     */
    public void commitComment(int topicId, int topicCommentId, int answeredUserId, String content) {
        mApiHelper.executeForData(mRootView,
                mModel.topicComment(topicId, topicCommentId, answeredUserId, content),
                new ErrorHandleSubscriber(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        mRootView.onCommitCommentSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mRootView.showToastLong(e.getMessage());
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }


    /**
     * 话题评论点赞/取消评论点赞
     * @param topicId 话题ID
     * @param comment 话题评论
     *  type 类型1点赞 2取消点赞
     */
    public void commentSupport(int topicId, Comment comment) {
        mApiHelper.executeForData(mRootView,
                mModel.topicCommentSupport(topicId, comment.getCommentId(),
                        comment.getIsSupport() == 0 ? "1" : "2"),
                new ErrorHandleSubscriber(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        if (comment.getIsSupport() == 0) {
                            // 点赞成功
                            comment.setIsSupport(1);
                            comment.setSupportTotal(comment.getSupportTotal() + 1);
                        } else {
                            // 取消点赞
                            comment.setIsSupport(0);
                            comment.setSupportTotal(comment.getSupportTotal() - 1);
                        }

                        mRootView.onSupportSuccess(comment);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mRootView.showToastLong(e.getMessage());
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    /**
     * 话题评论列表
     * @param topicId 话题ID
     * @param refresh 是否刷新
     */
    public void loadCommentList(int topicId, int commentId, boolean refresh) {
        if (refresh) {
            mCurrent = 0;
            mCommentList.clear();

            // 如果记录的评论不为空，则添加到顶部，这是二级页面的主评论
            if (null != mComment) {
                mCommentList.add(mComment);
            }
        }

        mApiHelper.execute(mRootView,
                mModel.commentList(topicId, commentId, mSize, mCurrent + 1),
                new ErrorHandleSubscriber<CommentRsp>(mErrorHandler) {
                    @Override
                    public void onNext(CommentRsp rsp) {
                        if (null != rsp.getPage()) {
                            Comments page = rsp.getPage();

                            mCurrent = page.getCurrent();

                            if (null != page.getRecords()) {
                                mCommentList.addAll(page.getRecords());
                            }
                        }

                        // 评论总数
                        mRootView.setReplyCount(rsp.getCount());

                        mRootView.onLoadCommentSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                },
                disposable -> {},
                () -> mRootView.onLoadCommentCompleted());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}