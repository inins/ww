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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}