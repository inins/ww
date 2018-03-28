package com.wang.social.login.mvp.presenter;

import com.wang.social.login.mvp.contract.LoginContract;
import com.wang.social.login.mvp.model.entities.LoginInfo;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:13
 * ========================================
 */
@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View view) {
        super(model, view);
    }



    public void login(String mobile, String password) {
        mApiHelper.execute(mRootView, mModel.login(mobile, password),
                new ErrorHandleSubscriber<LoginInfo>(mErrorHandler) {

                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        Timber.d("Token:" + loginInfo.getToken());
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