package com.wang.social.login.mvp.presenter;

import com.frame.utils.ToastUtil;
import com.wang.social.login.mvp.contract.LoginContract;
import com.wang.social.login.mvp.model.entities.LoginInfo;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;

import java.sql.Time;
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
        mApiHelper.execute(mRootView,
                mModel.passwordLogin(mobile, password, ""),
                new ErrorHandleSubscriber<LoginInfo>(mErrorHandler) {

                    @Override
                    public void onNext(LoginInfo loginInfo) {
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

    /**
     * 短信登录
     * @param mobile
     * @param code
     */
    public void messageLogin(String mobile, String code) {
        mApiHelper.execute(mRootView,
                mModel.verifyCodeLogin(mobile, code, "", ""),
                new ErrorHandleSubscriber<LoginInfo>(mErrorHandler) {

                    @Override
                    public void onNext(LoginInfo loginInfo) {
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

    /**
     * 注册
     * @param mobile
     * @param code
     * @param password
     */
    public void register(String mobile, String code, String password) {
        mApiHelper.execute(mRootView,
                mModel.userRegister(mobile, code, password, ""),
                new ErrorHandleSubscriber(mErrorHandler) {

                    @Override
                    public void onNext(Object o) {
                        // 提示注册成功
                        mRootView.showToast("注册成功");
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

    /**
     * 手机号码加短信验证码登录
     * @param mobile 手机号码
     * @param type 用途类型
     *
    用途类型
    （注册 type=1;
    找回密码 type=2;
    三方账号绑定手机 type=4;
    更换手机号 type=5;
    短信登录 type=6）
     * @return
     */
    public void sendVerifyCode(String mobile, int type) {
        mApiHelper.executeForData(mRootView,
                mModel.sendVerifyCode(mobile, type, ""),
                new ErrorHandleSubscriber(mErrorHandler) {

                    @Override
                    public void onNext(Object o) {
                        mRootView.showToast(o.toString());
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