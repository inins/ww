package com.wang.social.login.mvp.presenter;

import com.frame.component.api.Api;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
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
                        // 验证失败
                        mRootView.onCheckVerifyCodeFailed(e.getMessage());
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
     *
    用途类型
    （注册 type=1;
    找回密码 type=2;
    三方账号绑定手机 type=4;
    更换手机号 type=5;
    短信登录 type=6）
     * @return
     */
    public void sendVerifyCode(String mobile) {
        mApiHelper.executeForData(mRootView,
                mModel.sendVerifyCode(mobile, 2), // 找回密码 type=2
                new ErrorHandleSubscriber(mErrorHandler) {

                    @Override
                    public void onNext(Object o) {
//                        mRootView.showToast(o.toString());

                        mRootView.onSendVerifyCodeSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToast(e.getMessage());
                    }

                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}