package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiException;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.CTSocialListContract;
import com.wang.social.im.mvp.model.entities.ListData;
import com.wang.social.im.mvp.model.entities.SimpleGroupInfo;
import com.wang.social.im.mvp.model.entities.SimpleSocial;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-06-14 10:11
 * ============================================
 */
@ActivityScope
public class CTSocialListPresenter extends BasePresenter<CTSocialListContract.Model, CTSocialListContract.View> {

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mRxErrorHandler;

    @Inject
    public CTSocialListPresenter(CTSocialListContract.Model model, CTSocialListContract.View view) {
        super(model, view);
    }

    public void getSocialList() {
        mApiHelper.execute(mRootView, mModel.getSocials(),
                new ErrorHandleSubscriber<ListData<SimpleSocial>>(mRxErrorHandler) {
                    @Override
                    public void onNext(ListData<SimpleSocial> simpleGroupInfoListData) {
                        mRootView.showSocials(simpleGroupInfoListData.getList());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showSocials(null);
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