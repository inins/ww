package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.wang.social.im.mvp.contract.CreateEnvelopContract;
import com.wang.social.im.mvp.model.entities.CreateEnvelopResult;
import com.wang.social.im.mvp.model.entities.EnvelopInfo;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-24 10:04
 * ============================================
 */
@ActivityScope
public class CreateEnvelopPresenter extends BasePresenter<CreateEnvelopContract.Model, CreateEnvelopContract.View> {

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public CreateEnvelopPresenter(CreateEnvelopContract.Model model, CreateEnvelopContract.View view) {
        super(model, view);
    }

    /**
     * 创建红包
     * @param envelopType
     * @param diamond
     * @param count
     * @param message
     * @param groupId
     */
    public void createEnvelop(EnvelopInfo.EnvelopType envelopType, int diamond, int count, String message, String groupId) {
        mApiHelper.execute(mRootView,
                mModel.createEnvelop(envelopType, diamond, count, message, groupId), new ErrorHandleSubscriber<CreateEnvelopResult>(mErrorHandler) {
                    @Override
                    public void onNext(CreateEnvelopResult result) {
                        mRootView.onCreateSuccess(result.getEnvelopId(), message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
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
