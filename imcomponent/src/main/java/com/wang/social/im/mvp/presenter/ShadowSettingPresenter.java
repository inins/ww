package com.wang.social.im.mvp.presenter;

import com.frame.component.app.Constant;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.helper.QiNiuManager;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.mvp.contract.ShadowSettingContract;
import com.wang.social.im.mvp.model.entities.PayCheckInfo;
import com.wang.social.im.mvp.model.entities.ShadowInfo;

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
    IRepositoryManager mRepositoryManager;

    @Inject
    public ShadowSettingPresenter(ShadowSettingContract.Model model, ShadowSettingContract.View view) {
        super(model, view);
    }

    /**
     * 修改分身信息
     */
    public void updateShadowInfo(ShadowInfo info) {
        //检查是否需要支付
        mApiHelper.execute(mRootView, mModel.checkShadowStatus(info.getSocialId()),
                new ErrorHandleSubscriber<PayCheckInfo>(mErrorHandler) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mRootView.showLoading();
                    }

                    @Override
                    public void onNext(PayCheckInfo shadowCheckInfo) {
                        if (shadowCheckInfo.getCheckState() == PayCheckInfo.STATUS_NEED) { //需要支付
                            mRootView.hideLoading();
                            mRootView.showPayDialog(shadowCheckInfo.getApplyId(), info);
                        } else {//无需支付
                            doUpdate(shadowCheckInfo.getApplyId(), info);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.hideLoading();
                    }
                });
    }

    public void payForUpdate(String applyId, ShadowInfo info) {
        NetPayStoneHelper.newInstance().stonePay(mRootView, IMConstants.UPDATE_SHADOW_PRICE, Constant.PAY_OBJECT_TYPE_SHADOW, applyId, new NetPayStoneHelper.OnStonePayCallback() {
            @Override
            public void success() {
                doUpdate(applyId, info);
            }
        });
    }

    private void doUpdate(String applyId, ShadowInfo info) {
        //判断是否需要上传头像
        if (!info.getPortrait().startsWith("http")) {
            QiNiuManager qiNiuManager = new QiNiuManager(mRepositoryManager);
            qiNiuManager.uploadFile(mRootView, info.getPortrait(), new QiNiuManager.OnSingleUploadListener() {
                @Override
                public void onSuccess(String url) {
                    info.setPortrait(url);
                    update(applyId, info);
                }

                @Override
                public void onFail() {
                    mRootView.hideLoading();
                    ToastUtil.showToastShort("头像上传失败");
                }
            });
        } else {
            update(applyId, info);
        }
    }

    private void update(String applyId, ShadowInfo info) {
        mApiHelper.executeNone(mRootView, mModel.updateShadowInfo(info.getSocialId(), applyId, info.getNickname(), info.getPortrait(), info.getGender()),
                new ErrorHandleSubscriber<BaseJson>() {
                    @Override
                    public void onNext(BaseJson o) {
                        //修改成功
                        mRootView.onUpdateComplete(info);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
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