package com.wang.social.topic.mvp.presenter;

import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.TopicListContract;
import com.wang.social.topic.mvp.model.entities.Topic;
import com.wang.social.topic.mvp.model.entities.TopicRsp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


@FragmentScope
public class TopicListPresenter extends
        BasePresenter<TopicListContract.Model, TopicListContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    List<Topic> mTopicList = new ArrayList<>();

    // 每页条数
    int mSize = 10;
    // 当前页码
    int mCurrent = 0;

    @Inject
    public TopicListPresenter(TopicListContract.Model model, TopicListContract.View view) {
        super(model, view);
    }

    public Topic getTopic(int position) {

        if (position >= 0 && position < mTopicList.size()) {
            return mTopicList.get(position);
        }

        return null;
    }

    public int getTopicCount() {
        return mTopicList.size();
    }

    public void clearTopicList() {
        mTopicList.clear();
    }

    /**
     * 或者最新话题列表
     */
    public void getNewsList() {
        mApiHelper.execute(mRootView,
                mModel.getNewsList("1", mSize, mCurrent + 1),
                new ErrorHandleSubscriber<TopicRsp>(mErrorHandler) {

                    @Override
                    public void onNext(TopicRsp rsp) {
                        if (null != rsp) {
                            mCurrent = rsp.getCurrent();

                            mTopicList.addAll(rsp.getList());

                            mRootView.onTopicLoadSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToast(e.getMessage());
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.onTopicLoadCompleted();
                        mRootView.hideLoading();
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