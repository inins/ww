package com.wang.social.moneytree.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.moneytree.mvp.contract.GameListContract;
import com.wang.social.moneytree.mvp.model.entities.GameBean;
import com.wang.social.moneytree.mvp.model.entities.GameBeans;
import com.wang.social.moneytree.mvp.model.entities.NewGame;

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
                        mRootView.showToastLong(e.getMessage());
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

    /**
     * 创建游戏
     *
     * @param groupId   群ID (在群中创建时必传)
     * @param type      创建类型（1：通过群，2：用户）
     * @param gameType  游戏类型（1：人数，2：时间）
     * @param resetTime 重置时长(s)
     * @param diamond   钻石数
     * @param peopleNum 开始人数 (gameType=1时必传)
     */
    public void createGame(int groupId, int type, int gameType,
                           int resetTime, int diamond, int peopleNum) {
        mApiHelper.execute(mRootView,
                mModel.createGame(groupId, type, gameType, resetTime, diamond, peopleNum),
                new ErrorHandleSubscriber<NewGame>(mErrorHandler) {
                    @Override
                    public void onNext(NewGame newGame) {
                        mRootView.onCreateGameSuccess(newGame);
                    }
                    @Override
                    public void onError(Throwable e) {
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
                        mRootView.onCreateGameCompleted();
                        mRootView.hideLoading();
                    }
                });
    }

    private void testGameListData() {
        GameBean bean = new GameBean();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}