package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.ShadowSettingContract;
import com.wang.social.im.mvp.model.entities.ShadowCheckInfo;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 16:48
 * ============================================
 */
@ActivityScope
public class ShadowSettingPresenter extends BasePresenter<ShadowSettingContract.Model, ShadowSettingContract.View> {

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public ShadowSettingPresenter(ShadowSettingContract.Model model, ShadowSettingContract.View view) {
        super(model, view);
    }

    /**
     * 修改分身信息
     *
     * @param socialId
     * @param nickname
     * @param portrait
     */
    public void updateShadowInfo(String socialId, String nickname, String portrait) {
        //检查是否需要支付
        mApiHelper.execute(mRootView, mModel.checkShadowStatus(socialId),
                new ErrorHandleSubscriber<ShadowCheckInfo>(mErrorHandler) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mRootView.showLoading();
                    }

                    @Override
                    public void onNext(ShadowCheckInfo shadowCheckInfo) {
                        if (shadowCheckInfo.getCheckState() == ShadowCheckInfo.STATUS_NEED) { //需要支付

                        } else {//无需支付

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.hideLoading();
                    }
                });
    }

    private void doUpdate(String socialId, String orderId, String nickname, String portrait) {
        mApiHelper.execute(mRootView, mModel.updateShadowInfo(socialId, orderId, nickname, portrait),
                new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        //修改成功
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        //修改失败
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
}