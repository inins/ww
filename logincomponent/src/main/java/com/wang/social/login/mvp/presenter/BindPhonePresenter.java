package com.wang.social.login.mvp.presenter;

import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.login.mvp.contract.BindPhoneContract;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


@ActivityScope
public class BindPhonePresenter extends
        BasePresenter<BindPhoneContract.Model, BindPhoneContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    private String mMobile;

    @Inject
    public BindPhonePresenter(BindPhoneContract.Model model, BindPhoneContract.View view) {
        super(model, view);
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
        mMobile = mobile;

        mApiHelper.executeForData(mRootView,
                mModel.sendVerifyCode(mobile, type),
                new ErrorHandleSubscriber(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        // 成功获取验证码
                        mRootView.onSendVerifyCodeSuccess(mMobile);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToast(e.getMessage());
                    }
                }
                , new Consumer<Disposable>() {
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


    public void replaceMobile(String mobile, String code) {
        mApiHelper.executeForData(mRootView,
                mModel.replaceMobile(mobile, code),
                new ErrorHandleSubscriber(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        // 将绑定 到手机号码写入UserInfo
                        User user  = AppDataHelper.getUser();
                        user.setPhone(mobile);
                        AppDataHelper.saveUser(user);
                        mRootView.onReplaceMobileSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToast(e.getMessage());
                    }
                }
                , new Consumer<Disposable>() {
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