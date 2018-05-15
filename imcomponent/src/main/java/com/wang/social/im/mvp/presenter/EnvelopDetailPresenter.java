package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.PageList;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.EnvelopDetailContract;
import com.wang.social.im.mvp.model.entities.EnvelopAdoptInfo;

import javax.inject.Inject;

/**
 * ============================================
 * 为{@link com.wang.social.im.mvp.ui.EnvelopDetailActivity}提供逻辑处理
 * <p>
 * Create by ChenJing on 2018-04-24 13:50
 * ============================================
 */
@ActivityScope
public class EnvelopDetailPresenter extends BasePresenter<EnvelopDetailContract.Model, EnvelopDetailContract.View> {

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;

    private int currentPage = 0;

    @Inject
    public EnvelopDetailPresenter(EnvelopDetailContract.Model model, EnvelopDetailContract.View view) {
        super(model, view);
    }

    /**
     * 获取红包领取记录
     *
     * @param envelopId
     */
    public void getEnvelopAdoptInfo(long envelopId) {
        currentPage++;
        mApiHelper.execute(mRootView, mModel.getAdoptList(envelopId, currentPage), new ErrorHandleSubscriber<PageList<EnvelopAdoptInfo>>() {
            @Override
            public void onNext(PageList<EnvelopAdoptInfo> envelopAdoptInfoPageList) {
                mRootView.showList(envelopAdoptInfoPageList.getList(), envelopAdoptInfoPageList.getMaxPage() - envelopAdoptInfoPageList.getCurrent() > 0);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }
}
