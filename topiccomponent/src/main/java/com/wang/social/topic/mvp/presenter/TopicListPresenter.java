package com.wang.social.topic.mvp.presenter;

import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.TopicListContract;
import com.wang.social.topic.mvp.model.entities.Tag;
import com.wang.social.topic.mvp.model.entities.Tags;
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

    @Inject
    public TopicListPresenter(TopicListContract.Model model, TopicListContract.View view) {
        super(model, view);
    }

    public Topic getTopic(int posistion) {
        if (posistion >= 0 && posistion < mTopicList.size()) {
            return mTopicList.get(posistion);
        }

        return null;
    }

    public int getTopicCount() {
        return mTopicList.size();
    }

    /**
     * 或者最新话题列表
     */
    public void getNewsList() {
        mApiHelper.execute(mRootView,
                mModel.getNewsList("1", 20, 1),
                new ErrorHandleSubscriber<TopicRsp>(mErrorHandler) {

                    @Override
                    public void onNext(TopicRsp rsp) {
                        if (null != rsp) {
                            mTopicList.addAll(rsp.getList());

                            mRootView.onTopicLoaded();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}