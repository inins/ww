package com.wang.social.topic.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.SearchContract;
import com.wang.social.topic.mvp.model.entities.Comments;
import com.wang.social.topic.mvp.model.entities.SearchResult;
import com.wang.social.topic.mvp.model.entities.SearchResults;
import com.wang.social.topic.mvp.model.entities.Topic;
import com.wang.social.topic.mvp.model.entities.TopicRsp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


@ActivityScope
public class SearchPresenter extends
        BasePresenter<SearchContract.Model, SearchContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    private List<SearchResult> mResultList = new ArrayList<>();

    // 每页条数
    private int mSize = 10;
    // 当前页码
    private int mCurrent = 0;

    @Inject
    public SearchPresenter(SearchContract.Model model, SearchContract.View view) {
        super(model, view);
    }

    public List<SearchResult> getResultList() {
        return mResultList;
    }

    public void searchTopic(String keyword, String tagNames, boolean refresh) {
        if (refresh) {
            mCurrent = 0;
            mResultList.clear();
        }

        mApiHelper.execute(mRootView,
                mModel.searchTopic(keyword, tagNames, mSize, mCurrent + 1),
                new ErrorHandleSubscriber<SearchResults>(mErrorHandler) {
                    @Override
                    public void onNext(SearchResults results) {
                        if (results.getList().size() > 0) {
                            mCurrent = results.getCurrent();
                        }

                        mResultList.addAll(results.getList());

                        mRootView.onSearchSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToast(e.getMessage());
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.onSearchCompleted();
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