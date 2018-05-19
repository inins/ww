package com.wang.social.moneytree.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.moneytree.mvp.contract.GameListContract;
import com.frame.component.view.moneytree.PayHelper;
import com.wang.social.moneytree.mvp.model.entities.GameBean;
import com.wang.social.moneytree.mvp.model.entities.GameBeans;
import com.frame.component.entities.NewMoneyTreeGame;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


@ActivityScope
public class GameListPresenter extends
        BasePresenter<GameListContract.Model, GameListContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    private List<GameBean> mGameList = new ArrayList<>();
    // 每页条数
    private int mSize = 10;
    // 当前页码
    private int mCurrent = 0;

    @Inject
    public GameListPresenter(GameListContract.Model model, GameListContract.View view) {
        super(model, view);
    }

    public List<GameBean> getGameList() {
        return mGameList;
    }

    public void getMoneyTreeList(boolean refresh) {
        if (refresh) {
            mCurrent = 0;
            mGameList.clear();
        }

        mApiHelper.execute(mRootView,
                mModel.getMoneyTreeList(mSize, mCurrent + 1),
                new ErrorHandleSubscriber<GameBeans>(mErrorHandler) {
                    @Override
                    public void onNext(GameBeans results) {
                        if (results.getList().size() > 0) {
                            mCurrent = results.getCurrent();
                        }

                        mGameList.addAll(results.getList());

                        mRootView.onLoadGameListSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToastShort(e.getMessage());
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.onLoadGameListCompleted();
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