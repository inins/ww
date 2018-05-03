package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.wang.social.im.mvp.contract.TeamHomeContract;
import com.wang.social.im.mvp.model.entities.TeamInfo;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 15:17
 * ============================================
 */
@ActivityScope
public class TeamHomePresenter extends GroupPresenter<TeamHomeContract.Model, TeamHomeContract.View>{

    @Inject
    public TeamHomePresenter(TeamHomeContract.Model model, TeamHomeContract.View view) {
        super(model, view);
    }

    /**
     * 获取觅聊详情
     * @param teamId
     */
    public void getTeamInfo(String teamId){
        mApiHelper.execute(mRootView, mModel.getTeamInfo(teamId),
                new ErrorHandleSubscriber<TeamInfo>(mErrorHandler) {
                    @Override
                    public void onNext(TeamInfo teamInfo) {
                        mRootView.showTeamInfo(teamInfo);
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
}