package com.wang.social.login.mvp.presenter;

import com.wang.social.login.mvp.contract.LoginContract;
import com.wang.social.login.mvp.model.entities.LoginInfo;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    /**
     * 密码登录
     * @param mobile
     * @param password
     */
    public void passwordLogin(String mobile, String password) {
        Observable.interval(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRootView.showLoading();
                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mRootView.hideLoading();
                    }
                });
    }

    /**
     * 短信登录
     * @param mobile
     * @param code
     */
    public void messageLogin(String mobile, String code) {

    }

    /**
     * 注册
     * @param mobile
     * @param code
     * @param password
     */
    public void register(String mobile, String code, String password) {

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