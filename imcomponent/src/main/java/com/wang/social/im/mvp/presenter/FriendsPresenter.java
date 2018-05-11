package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.FriendsContract;
import com.wang.social.im.mvp.model.entities.IndexFriendInfo;
import com.wang.social.im.mvp.model.entities.ListData;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-08 9:40
 * ============================================
 */
@FragmentScope
public class FriendsPresenter extends BasePresenter<FriendsContract.Model, FriendsContract.View> {

    @Inject
    ApiHelper mApiHelper;

    @Inject
    public FriendsPresenter(FriendsContract.Model model, FriendsContract.View view) {
        super(model, view);
    }

    public void getFriendsList() {
        mApiHelper.execute(mRootView, mModel.getFriendList(),
                new ErrorHandleSubscriber<ListData<IndexFriendInfo>>() {
                    @Override
                    public void onNext(ListData<IndexFriendInfo> friendInfoListData) {
                        mRootView.showFriends(friendInfoListData.getList());
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