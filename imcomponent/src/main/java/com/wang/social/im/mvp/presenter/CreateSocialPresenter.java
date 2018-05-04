package com.wang.social.im.mvp.presenter;

import com.frame.component.helper.QiNiuManager;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.wang.social.im.mvp.contract.CreateSocialContract;
import com.wang.social.im.mvp.model.entities.CreateGroupResult;
import com.wang.social.im.mvp.model.entities.PayCheckInfo;
import com.wang.social.im.mvp.model.entities.SocialAttribute;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 20:26
 * ============================================
 */
@ActivityScope
public class CreateSocialPresenter extends BasePresenter<CreateSocialContract.Model, CreateSocialContract.View> {

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    IRepositoryManager mRepositoryManager;

    @Inject
    public CreateSocialPresenter(CreateSocialContract.Model model, CreateSocialContract.View view) {
        super(model, view);
    }

    public void checkPayStatus(SocialAttribute socialAttribute, String cover, String name, boolean canCreateTeam, String tags) {
        mApiHelper.execute(mRootView, mModel.checkCreateSocialStatus(),
                new ErrorHandleSubscriber<PayCheckInfo>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mRootView.showLoading();
                    }

                    @Override
                    public void onNext(PayCheckInfo payCheckInfo) {
                        if (payCheckInfo.getCheckState() == PayCheckInfo.STATUS_WITHOUT) {
                            createSocial(payCheckInfo.getOrderId(), cover, name, canCreateTeam, socialAttribute, tags);
                        }else {
                            mRootView.showPayDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.hideLoading();
                    }
                });
    }

    public void createSocial(String orderId, String cover, String name, boolean canCreateTeam, SocialAttribute socialAttribute, String tags) {
        //判断是否需要上传图片
        if (cover != null && !cover.startsWith("http")){
            QiNiuManager manager = new QiNiuManager(mRepositoryManager);
            mRootView.showLoading();
            manager.uploadFile(mRootView, cover, new QiNiuManager.OnSingleUploadListener(){

                @Override
                public void onSuccess(String url) {
                    doCreate(orderId, url, name, canCreateTeam, socialAttribute, tags);
                }

                @Override
                public void onFail() {
                    mRootView.hideLoading();
                    ToastUtil.showToastShort("封面上传失败");
                }
            });
        }else {
            doCreate(orderId, cover, name, canCreateTeam, socialAttribute, tags);
        }
    }

    private void doCreate(String orderId, String cover, String name, boolean canCreateTeam, SocialAttribute socialAttribute, String tags){
        mApiHelper.execute(mRootView,
                mModel.createSocial(orderId, name, "", cover, cover, socialAttribute, canCreateTeam, tags),
                new ErrorHandleSubscriber<CreateGroupResult>() {
                    @Override
                    public void onNext(CreateGroupResult createGroupResult) {

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