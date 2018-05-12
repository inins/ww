package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.InviteFriendContract;
import com.wang.social.im.mvp.model.entities.IndexFriendInfo;
import com.wang.social.im.mvp.model.entities.ListData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-12 14:47
 * ============================================
 */
@ActivityScope
public class InviteFriendPresenter extends BasePresenter<InviteFriendContract.Model, InviteFriendContract.View> {

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public InviteFriendPresenter(InviteFriendContract.Model model, InviteFriendContract.View view) {
        super(model, view);
    }

    /**
     * 获取好友列表
     *
     * @param socialId
     */
    public void getInviteFriendList(String socialId) {
        mApiHelper.execute(mRootView, mModel.getInviteFriendList(socialId),
                new ErrorHandleSubscriber<ListData<IndexFriendInfo>>() {
                    @Override
                    public void onNext(ListData<IndexFriendInfo> indexFriendInfoListData) {
                        mRootView.showFriends(indexFriendInfoListData.getList());
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
     * 邀请完成
     *
     * @param socialId
     * @param friends
     */
    public void sendInvite(String socialId, List<IndexFriendInfo> friends) {
        mApiHelper.executeNone(mRootView, mModel.sendInvite(socialId, friends),
                new ErrorHandleSubscriber<BaseJson>() {
                    @Override
                    public void onNext(BaseJson baseJson) {
                        mRootView.inviteComplete();
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
