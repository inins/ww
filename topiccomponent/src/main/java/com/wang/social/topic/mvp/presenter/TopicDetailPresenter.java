package com.wang.social.topic.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.TopicDetailContract;
import com.wang.social.topic.mvp.model.entities.TopicDetail;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@ActivityScope
public class TopicDetailPresenter extends
        BasePresenter<TopicDetailContract.Model, TopicDetailContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;


    @Inject
    public TopicDetailPresenter(TopicDetailContract.Model model, TopicDetailContract.View view) {
        super(model, view);
    }

    public void getTopicDetails(int topicId) {

            mApiHelper.execute(mRootView,
                    mModel.getTopicDetails(topicId),
                    new ErrorHandleSubscriber<TopicDetail>(mErrorHandler) {
                        @Override
                        public void onNext(TopicDetail topicDetail) {
                            // 成功获取验证码
                            mRootView.onTopicDetailLoadSuccess(topicDetail);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mRootView.showToast(e.getMessage());
                        }
                    }
                    , new Consumer<Disposable>() {
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