package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.AlertUserListContract;
import com.wang.social.im.mvp.contract.MemberListContract;
import com.wang.social.im.mvp.model.entities.IndexMemberInfo;
import com.wang.social.im.mvp.model.entities.ListData;
import com.wang.social.im.mvp.model.entities.MemberInfo;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * ============================================
 * <p>
 * <p>
 * Create by ChenJing on 2018-05-12 17:21
 * ============================================
 */
@ActivityScope
public class AlertUserListPresenter extends BasePresenter<AlertUserListContract.Model, AlertUserListContract.View> {

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public AlertUserListPresenter(AlertUserListContract.Model model, AlertUserListContract.View view) {
        super(model, view);
    }

    public void getMemberList(String groupId) {
        mApiHelper.execute(mRootView, mModel.getAlertMembers(groupId),
                new ErrorHandleSubscriber<ListData<IndexMemberInfo>>() {
                    @Override
                    public void onNext(ListData<IndexMemberInfo> memberInfoListData) {
                        mRootView.showMembers(memberInfoListData.getList());
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
