package com.wang.social.topic.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.google.gson.Gson;
import com.wang.social.topic.mvp.contract.CommentContract;
import com.wang.social.topic.mvp.model.entities.Comment;
import com.wang.social.topic.mvp.model.entities.Comments;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@ActivityScope
public class CommentPresenter extends
        BasePresenter<CommentContract.Model, CommentContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    private int mCurrent = 0;
    private int mSize = 10;

    List<Comment> mCommentList = new ArrayList<>();

    @Inject
    public CommentPresenter(CommentContract.Model model, CommentContract.View view) {
        super(model, view);
    }

    public List<Comment> getCommentList() {
        return mCommentList;
    }

    public void testLoadCommentList() {
        String json1 = "{\n" +
                "               \"commentId\":1,\n" +
                "               \"userId\":10000,\n" +
                "               \"avatar\":\"http://static.wangsocial.com/userDefault.png\",\n" +
                "               \"nickname\":\"婕欧巴的锅\",\n" +
                "               \"content\":\"你好看，你最美！宇宙无敌就是你！!\",\n" +
                "               \"supportTotal\":338,\n" +
                "               \"commentReply\":[{\n" +
                "               \t\t\t\"commentId\":2,\n" +
                "               \t\t\t\"userId\":10000,\n" +
                "\t\t\t            \"avatar\":\"http://static.wangsocial.com/userDefault.png\",\n" +
                "\t\t\t            \"nickname\":\"卡西莫多\",\n" +
                "\t\t\t            \"content\":\"洛洛洛霞\"\n" +
                "               \t\t},{\n" +
                "               \t\t\t\"commentId\":3,\n" +
                "               \t\t\t\"userId\":10000,\n" +
                "\t\t                \"avatar\":\"http://static.wangsocial.com/userDefault.png\",\n" +
                "\t\t                \"nickname\":\"Mongo\",\n" +
                "\t\t                \"content\":\"洛洛洛霞\"\n" +
                "               \t\t}]\n" +
                "            }";

        String json2 = "{\n" +
                "               \"commentId\":1,\n" +
                "               \"userId\":10000,\n" +
                "               \"avatar\":\"http://static.wangsocial.com/userDefault.png\",\n" +
                "               \"nickname\":\"芒果小姐\",\n" +
                "               \"content\":\"世界以痛吻我，我却报之以歌。\",\n" +
                "               \"supportTotal\":1,\n" +
                "               \"commentReply\":[{\n" +
                "               \t\t\t\"commentId\":1   ,\n" +
                "               \t\t\t\"userId\":10000,\n" +
                "\t\t\t            \"avatar\":\"http://static.wangsocial.com/userDefault.png\",\n" +
                "\t\t\t            \"nickname\":\"林飞\",\n" +
                "\t\t\t            \"content\":\"给你在小黑屋加鸡腿。给你在小黑屋加鸡腿。给你在小黑屋加鸡腿。给你在小黑屋加鸡腿。给你在小黑屋加鸡腿。给你在小黑屋加鸡腿。给你在小黑屋加鸡腿。\"\n" +
                "               \t\t},{\n" +
                "               \t\t\t\"commentId\":3,\n" +
                "               \t\t\t\"userId\":10000,\n" +
                "\t\t                \"avatar\":\"http://static.wangsocial.com/userDefault.png\",\n" +
                "\t\t                \"nickname\":\"Mongo\",\n" +
                "\t\t                \"content\":\"洛洛洛霞\"\n" +
                "               \t\t}]\n" +
                "            }";


        Comment comment1 = new Gson().fromJson(json1, Comment.class);
        Comment comment2 = new Gson().fromJson(json2, Comment.class);

        for (int i = 0; i < 5; i++) {
            if (i % 2 == 0) {
                mCommentList.add(comment1);
            } else {
                mCommentList.add(comment2);
            }
        }

        mRootView.onLoadCommentSuccess();
        mRootView.onLoadCommentCompleted();
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
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                });
    }


    /**
     * 话题评论点赞/取消评论点赞
     * @param topicId 话题ID
     * @param comment 话题评论
     * @Note type 类型1点赞 2取消点赞
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
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                });
    }

    /**
     * 话题评论列表
     * @param topicId 话题ID
     * @param refresh 是否刷新
     * @return
     */
    public void loadCommentList(int topicId, int commentId, boolean refresh) {
        if (refresh) {
            mCurrent = 0;
            mCommentList.clear();
        }

        mApiHelper.execute(mRootView,
                mModel.commentList(topicId, commentId, mSize, mCurrent + 1),
                new ErrorHandleSubscriber<Comments>(mErrorHandler) {
                    @Override
                    public void onNext(Comments comments) {
                        mRootView.setReplyCount(comments.getTotal());

                        mCurrent = comments.getCurrent();

                        mCommentList.addAll(comments.getList());

                        mRootView.onLoadCommentSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.onLoadCommentCompleted();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}