package com.wang.social.im.mvp.presenter;

import com.frame.component.helper.QiNiuManager;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.wang.social.im.mvp.contract.CreateTeamContract;
import com.wang.social.im.mvp.model.entities.CreateGroupResult;
import com.wang.social.im.mvp.model.entities.PayCheckInfo;
import com.wang.social.im.mvp.model.entities.TeamAttribute;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-03 14:06
 * ============================================
 */
@ActivityScope
public class CreateTeamPresenter extends BasePresenter<CreateTeamContract.Model, CreateTeamContract.View> {

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    IRepositoryManager mRepositoryManager;

    @Inject
    public CreateTeamPresenter(CreateTeamContract.Model model, CreateTeamContract.View view) {
        super(model, view);
    }

    /**
     * 检查创建状态
     *
     * @param name
     * @param cover
     * @param tag
     * @param attr
     */
    public void checkCreateStatus(String socialId, String name, String cover, String tag, TeamAttribute attr) {
        mApiHelper.execute(mRootView, mModel.checkCreateTeamStatus(socialId, tag),
                new ErrorHandleSubscriber<PayCheckInfo>() {
                    @Override
                    public void onNext(PayCheckInfo payCheckInfo) {
                        if (payCheckInfo.getCheckState() == PayCheckInfo.STATUS_WITHOUT) {
                            createTeam(payCheckInfo.getApplyId(), name, cover, tag, attr);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtil.showToastShort(e.getMessage());
                        mRootView.hideLoading();
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                });
    }

    public void createTeam(String createApplyId, String name, String cover, String tag, TeamAttribute attr) {
        //判断是否需要上传图片
        if (!cover.startsWith("http")) {
            QiNiuManager manager = new QiNiuManager(mRepositoryManager);
            manager.uploadFile(mRootView, cover, new QiNiuManager.OnSingleUploadListener() {
                @Override
                public void onSuccess(String url) {
                    doCreate(createApplyId, name, url, tag, attr);
                }

                @Override
                public void onFail() {
                    ToastUtil.showToastShort("封面上传失败");
                    mRootView.hideLoading();
                }
            });
        } else {
            doCreate(createApplyId, name, cover, tag, attr);
        }
    }

    private void doCreate(String createApplyId, String name, String cover, String tag, TeamAttribute attr) {
        mApiHelper.execute(mRootView, mModel.createTeam(createApplyId, cover, cover, name, tag, attr),
                new ErrorHandleSubscriber<CreateGroupResult>() {
                    @Override
                    public void onNext(CreateGroupResult result) {
                        mRootView.onCreateComplete(result);
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
