package com.wang.social.topic.mvp.presenter;

import com.frame.component.entities.Topic;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.PageList;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.TopicListContract;

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

    private List<Topic> mTopicList = new ArrayList<>();

    // 每页条数
    private int mSize = 10;
    // 当前页码
    private int mCurrent = 0;
    // 是否大量知识
    private boolean mTagAll = false;



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

    public List<Topic> getTopicList() {
        return mTopicList;
    }

    public void setTagAll(boolean tagAll) {
        mTagAll = tagAll;
    }

    public int getTopicCount() {
        return mTopicList.size();
    }

    public void clearTopicList() {
        mTopicList.clear();
        mCurrent = 0;
    }

    private void initHandler() {
        if (null == mErrorHandleSubscriber) {
            mErrorHandleSubscriber = new ErrorHandleSubscriber<PageList<Topic>>(mErrorHandler) {
                @Override
                public void onNext(PageList<Topic> pageList) {
                    if (null != pageList) {
                        mCurrent = pageList.getCurrent();

                        mTopicList.addAll(pageList.getList());

                        mRootView.onTopicLoadSuccess();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    mRootView.showToast(e.getMessage());
                }
            };
        }

        if (null == mConsumer) {
            mConsumer = new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mRootView.showLoading();
                }
            };
        }

        if (null == mAction) {
            mAction = new Action() {
                @Override
                public void run() throws Exception {
                    mRootView.onTopicLoadCompleted();
                    mRootView.hideLoading();
                }
            };
        }
    }

    private ErrorHandleSubscriber<PageList<Topic>> mErrorHandleSubscriber;
    private Consumer<Disposable> mConsumer;
    private Action mAction;

    /**
     * 或者最新话题列表
     */
    public void getNewsList() {
        initHandler();

        mApiHelper.execute(mRootView,
                mModel.getNewsList(mTagAll ? "1" : "0", mSize, mCurrent + 1),
                mErrorHandleSubscriber,
                mConsumer,
                mAction);

        setTagAll(false);
    }

    public void getHotList() {
        initHandler();

        mApiHelper.execute(mRootView,
                mModel.getHotList(mSize, mCurrent + 1),
                mErrorHandleSubscriber,
                mConsumer,
                mAction);
    }

    public void getLowList() {
        initHandler();

        mApiHelper.execute(mRootView,
                mModel.getLowList(mSize, mCurrent + 1),
                mErrorHandleSubscriber,
                mConsumer,
                mAction);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}