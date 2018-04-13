package com.wang.social.login.mvp.presenter;

import com.frame.component.api.Api;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.wang.social.login.mvp.contract.ResetPasswordContract;
import com.wang.social.login.mvp.model.entities.LoginInfo;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


@ActivityScope
public class ResetPasswordPresenter extends
        BasePresenter<ResetPasswordContract.Model, ResetPasswordContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    @Inject
    public ResetPasswordPresenter(ResetPasswordContract.Model model, ResetPasswordContract.View view) {
        super(model, view);
    }


    public void setPassword(String password) {
        mApiHelper.executeForData(mRootView,
                mModel.userSetPassword(password),
                new ErrorHandleSubscriber(mErrorHandler) {

                    @Override
                    public void onNext(Object o) {
                        mRootView.onSetPasswordSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
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


    public void resetPassword(String mobile, String code, String password) {
        mApiHelper.executeForData(mRootView,
                mModel.userForgetPassword(mobile, code, password),
                new ErrorHandleSubscriber(mErrorHandler) {

                    @Override
                    public void onNext(Object o) {
                        mRootView.onResetPasswordSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}