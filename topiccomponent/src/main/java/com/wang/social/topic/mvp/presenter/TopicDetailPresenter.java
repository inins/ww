package com.wang.social.topic.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.wang.social.topic.mvp.contract.TopicDetailContract;
import com.wang.social.topic.mvp.model.entities.TopicDetail;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

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

    private TopicDetail mTopicDetail;

    @Inject
    public TopicDetailPresenter(TopicDetailContract.Model model, TopicDetailContract.View view) {
        super(model, view);
    }

    /**
     * 获取话题详情
     * @param topicId 话题ID
     */
    public void getTopicDetails(int topicId) {
            mApiHelper.execute(mRootView,
                    mModel.getTopicDetails(topicId),
                    new ErrorHandleSubscriber<TopicDetail>(mErrorHandler) {
                        @Override
                        public void onNext(TopicDetail topicDetail) {
                            mTopicDetail = topicDetail;
                            // 获取详情成功
                            mRootView.onTopicDetailLoadSuccess(topicDetail);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
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

    /**
     * 举报（用户/话题/趣聊/趣晒）
     * 举报类型（0人 1趣聊 2趣晒 3主播 4 话题）
     */
    public void report() {
        mApiHelper.executeForData(mRootView,
                mModel.report(mTopicDetail.getTopicId(),
                        "4","", ""),
                new ErrorHandleSubscriber(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        mRootView.showToast("举报成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
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
                        mRootView.hideLoading();
                    }
                });
    }

    public void topicSupport() {
        mApiHelper.executeForData(mRootView,
                mModel.topicSupport(mTopicDetail.getTopicId(),
                        mTopicDetail.getIsSupport() == 0 ? "1" : "2"),
                new ErrorHandleSubscriber(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
//                        if (mTopicDetail.getIsSupport() == 0) {
//                            // 点赞成功
//                            mTopicDetail.setIsSupport(1);
//                            mTopicDetail.setSupportTotal(mTopicDetail.getSupportTotal() + 1);
//                        } else {
//                            // 取消点赞
//                            mTopicDetail.setIsSupport(0);
//                            mTopicDetail.setSupportTotal(mTopicDetail.getSupportTotal() - 1);
//                        }
//
//                        mRootView.resetSupportLayout(mTopicDetail.getIsSupport(), mTopicDetail.getSupportTotal());

                        // 发出通知点赞成功
                        EventBean eventBean = new EventBean(EventBean.EVENTBUS_TOPIC_SUPPORT);
                        eventBean.put("topicId", mTopicDetail.getTopicId());
                        eventBean.put("isSupport", mTopicDetail.getIsSupport() == 0);
                        EventBus.getDefault().post(eventBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }


    public void delMyTopic(int id) {
        mApiHelper.executeForData(mRootView,
                mModel.delMyTopic(id),
                new ErrorHandleSubscriber() {
                    @Override
                    public void onNext(Object o) {
                        mRootView.onDelMyTopicSuccess(id);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}