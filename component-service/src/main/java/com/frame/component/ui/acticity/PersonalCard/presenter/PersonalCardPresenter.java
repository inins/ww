package com.frame.component.ui.acticity.PersonalCard.presenter;

import com.frame.component.ui.acticity.PersonalCard.contract.PersonalCardContract;
import com.frame.component.ui.acticity.PersonalCard.model.entities.PersonalInfo;
import com.frame.component.ui.acticity.PersonalCard.model.entities.UserStatistics;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


@ActivityScope
public class PersonalCardPresenter extends
        BasePresenter<PersonalCardContract.Model, PersonalCardContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;


    @Inject
    public PersonalCardPresenter(PersonalCardContract.Model model, PersonalCardContract.View view) {
        super(model, view);
    }

    /**
     * 用户数据统计（我的/推荐/个人名片）
     *
     * @param userId 用户ID
     */
    public void loadUserStatistics(int userId) {
        mApiHelper.execute(mRootView,
                mModel.getUserStatistics(userId),
                new ErrorHandleSubscriber<UserStatistics>() {
                    @Override
                    public void onNext(UserStatistics userStatistics) {
                        mRootView.onLoadUserStatisticsSuccess(userStatistics);
                    }
                });
    }

    /**
     * 用户信息
     *
     * @param userId 用户id
     */
    public void loadUserInfoAndPhotos(int userId) {
        mApiHelper.execute(mRootView,
                mModel.getUserInfoAndPhotos(userId),
                new ErrorHandleSubscriber<PersonalInfo>() {
                    @Override
                    public void onNext(PersonalInfo userInfo) {
                        mRootView.onLoadUserInfoSuccess(userInfo);
                    }
                },
                new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                },
                new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                });
    }

    /**
     * 删除好友关系
     *
     * @param friendUserId 用户id
     */
    public void deleteFriend(int friendUserId) {
        mApiHelper.executeForData(mRootView,
                mModel.deleteFriend(friendUserId),
                new ErrorHandleSubscriber() {
                    @Override
                    public void onNext(Object o) {
                        mRootView.onDeleteFriendSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.toastLong(e.getMessage());
                    }
                });
    }

    /**
     * 拉黑或取消拉黑
     * @param blackUserId 用户id
     * @param black 是否拉黑
     */
    public void changeMyBlack(int blackUserId, boolean black) {
        mApiHelper.executeForData(mRootView,
                mModel.changeMyBlack(blackUserId, black),
                new ErrorHandleSubscriber() {
                    @Override
                    public void onNext(Object o) {
                        mRootView.onChangeMyBlackSuccess(!black);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.toastLong(e.getMessage());
                    }
                });
    }

    /**
     * 申请添加好友
     *
     * @param addUserId
     * @param reason
     */
    public void addFirendApply(int addUserId, String reason) {
        mApiHelper.executeForData(mRootView,
                mModel.addFriendApply(addUserId, reason),
                new ErrorHandleSubscriber() {
                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.toastLong(e.getMessage());
                    }
                },
                new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                },
                new Action() {
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