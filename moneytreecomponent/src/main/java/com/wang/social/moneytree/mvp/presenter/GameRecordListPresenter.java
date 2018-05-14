package com.wang.social.moneytree.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.moneytree.mvp.contract.GameRecordListContract;
import com.wang.social.moneytree.mvp.model.entities.GameBean;
import com.wang.social.moneytree.mvp.model.entities.GameBeans;
import com.wang.social.moneytree.mvp.model.entities.GameRecord;
import com.wang.social.moneytree.mvp.model.entities.GameRecords;
import com.wang.social.moneytree.mvp.model.entities.NewGame;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


@ActivityScope
public class GameRecordListPresenter extends
        BasePresenter<GameRecordListContract.Model, GameRecordListContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    private List<GameRecord> mRecordList = new ArrayList<>();
    // 每页条数
    private int mSize = 10;
    // 当前页码
    private int mCurrent = 0;

    @Inject
    public GameRecordListPresenter(GameRecordListContract.Model model, GameRecordListContract.View view) {
        super(model, view);
    }

    public List<GameRecord> getRecordList() {
        return mRecordList;
    }

    public void loadRecordList(boolean refresh, int type) {
        if (refresh) {
            mCurrent = 0;
            mRecordList.clear();
        }

        mApiHelper.execute(mRootView,
                mModel.getRecordList(mSize, mCurrent + 1, type),
                new ErrorHandleSubscriber<GameRecords>(mErrorHandler) {
                    @Override
                    public void onNext(GameRecords results) {
                        if (results.getList().size() > 0) {
                            mCurrent = results.getCurrent();
                        }

                        mRecordList.addAll(results.getList());

                        mRootView.onLoadRecordListSuccess();
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
                        mRootView.onLoadRecordListCompleted();
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