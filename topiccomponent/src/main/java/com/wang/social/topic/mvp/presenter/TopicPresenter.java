package com.wang.social.topic.mvp.presenter;

import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.TopicContract;
import com.wang.social.topic.mvp.model.entities.Tags;
import com.wang.social.topic.mvp.model.entities.TopicTopUser;
import com.wang.social.topic.mvp.model.entities.TopicTopUsers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


@FragmentScope
public class TopicPresenter extends
        BasePresenter<TopicContract.Model, TopicContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    List<TopicTopUser> mTopicTopUserList = new ArrayList<>();

    private int mSize = 10;
    private int mCurrent = 0;

    @Inject
    public TopicPresenter(TopicContract.Model model, TopicContract.View view) {
        super(model, view);
    }

    public List<TopicTopUser> getTopicTopUserList() {
        return mTopicTopUserList;
    }

    /**
     * 加载知识魔列表
     */
    public void getReleaseTopicTopUser(boolean refresh) {
        if (refresh) {
            mCurrent = 0;
            mTopicTopUserList.clear();
        }
        mApiHelper.execute(mRootView,
                mModel.getReleaseTopicTopUser(mSize, mCurrent + 1, "square"),
                new ErrorHandleSubscriber<TopicTopUsers>(mErrorHandler) {
                    @Override
                    public void onNext(TopicTopUsers topicTopUsers) {
                        if (topicTopUsers.getList() != null &&
                                topicTopUsers.getList().size() > 0) {
                            mCurrent++;
                        }

                        mTopicTopUserList.addAll(topicTopUsers.getList());

                        mRootView.onTopicTopUserLoadSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        mRootView.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
//                        mRootView.hideLoading();
                        mRootView.onTopicTopUserLoadCompleted();
                    }
                });
    }

    /**
     * 已选推荐标签列表(兴趣标签)
     */
    public void myRecommendTag() {
        mApiHelper.execute(mRootView,
                // 只加载3个
                mModel.myRecommendTag(3, 0),
                new ErrorHandleSubscriber<Tags>(mErrorHandler) {

                    @Override
                    public void onNext(Tags tags) {
                        if (null != tags && tags.getList() != null) {

                            // 更新已选标签列表UI
                            mRootView.onMyRecommendTagListLoad(tags.getList());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {


                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        mRootView.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
//                        mRootView.hideLoading();
                    }
                });
    }

    /**
     * 搜索
     */
    public void search() {
        mRootView.showToast("搜索");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}