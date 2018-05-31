package com.wang.social.im.mvp.presenter;

import com.frame.component.helper.QiNiuManager;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.ToastUtil;
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
public class TeamHomePresenter extends GroupPresenter<TeamHomeContract.Model, TeamHomeContract.View> {

    @Inject
    public TeamHomePresenter(TeamHomeContract.Model model, TeamHomeContract.View view) {
        super(model, view);
    }

    /**
     * 获取觅聊详情
     *
     * @param teamId
     */
    public void getTeamInfo(String teamId) {
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

    public void getSelfProfile(String teamId) {
        mApiHelper.execute(mRootView, mModel.getSelfProfile(teamId),
                new ErrorHandleSubscriber<TeamInfo>(mErrorHandler) {
                    @Override
                    public void onNext(TeamInfo teamInfo) {
                        if (teamInfo.getSelfProfile() != null) {
                            mRootView.showProfile(teamInfo.getSelfProfile());
                        }
                    }
                });
    }

    /**
     * 修改觅聊信息
     *
     * @param teamInfo
     */
    public void updateTeamInfo(TeamInfo teamInfo) {
        mApiHelper.executeNone(mRootView, mModel.updateTeamInfo(teamInfo)
                , new ErrorHandleSubscriber<BaseJson>() {
                    @Override
                    public void onNext(BaseJson baseJson) {
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

    /**
     * 修改觅聊图片
     *
     * @param cover
     * @param teamInfo
     */
    public void updateCover(String cover, TeamInfo teamInfo) {
        if (!cover.startsWith("http")) {
            QiNiuManager manager = new QiNiuManager(mRepositoryManager);
            manager.uploadFile(mRootView, cover, new QiNiuManager.OnSingleUploadListener() {
                @Override
                public void onSuccess(String url) {
                    teamInfo.setCover(url);
                    updateTeamInfo(teamInfo);
                }

                @Override
                public void onFail() {
                    ToastUtil.showToastShort("封面上传失败");
                    mRootView.hideLoading();
                }
            });
        } else {
            teamInfo.setCover(cover);
            updateTeamInfo(teamInfo);
        }
    }
}