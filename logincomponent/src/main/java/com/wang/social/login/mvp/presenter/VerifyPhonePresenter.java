package com.wang.social.login.mvp.presenter;

import com.frame.component.api.Api;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.login.mvp.contract.VerifyPhoneContract;
import com.wang.social.login.mvp.model.entities.LoginInfo;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


@ActivityScope
public class VerifyPhonePresenter extends
        BasePresenter<VerifyPhoneContract.Model, VerifyPhoneContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    // 记录输入的信息
    private String mMobile;
    private String mCode;

    @Inject
    public VerifyPhonePresenter(VerifyPhoneContract.Model model, VerifyPhoneContract.View view) {
        super(model, view);
    }

    /**
     * 验证验证码
     * @param mobile
     * @param code
     */
    public void checkVerifyCode(String mobile, String code) {
        mMobile = mobile;
        mCode = code;
        mApiHelper.executeForData(mRootView,
                mModel.preVerifyForForgetPassword(mobile, code),
                new ErrorHandleSubscriber(mErrorHandler) {

                    @Override
                    public void onNext(Object o) {
                        // 验证成功
                        mRootView.onCheckVerifyCodeSuccess(mMobile, mCode);
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