package com.frame.component.ui.acticity.PersonalCard.presenter;

import com.frame.component.ui.acticity.PersonalCard.contract.FriendListContract;
import com.frame.component.ui.acticity.PersonalCard.model.entities.FriendList;
import com.frame.component.ui.acticity.PersonalCard.model.entities.UserInfo;
import com.frame.component.ui.acticity.PersonalCard.model.entities.UserStatistics;
import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


@FragmentScope
public class FriendListPresenter extends
        BasePresenter<FriendListContract.Model, FriendListContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    private int mSize = 10;
    private int mCurrent = 0;
    private List<UserInfo> mUserInfoList = new ArrayList<>();

    @Inject
    public FriendListPresenter(FriendListContract.Model model, FriendListContract.View view) {
        super(model, view);
    }

    public List<UserInfo> getUserInfoList() {
        return mUserInfoList;
    }

    public void loadUserFriendList(int userId, boolean refresh) {
        if (refresh) {
            mCurrent = 0;
            mUserInfoList.clear();
        }
        mApiHelper.execute(mRootView,
                mModel.getUserFriendList(userId, mCurrent + 1, mSize),
                new ErrorHandleSubscriber<FriendList>() {
                    @Override
                    public void onNext(FriendList friendList) {
                        if (null != friendList && null != friendList.getList()) {
                            mUserInfoList.addAll(friendList.getList());
                        }

                        mRootView.onLoadFriendListSuccess();
                    }
                },
                new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                },
                new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.onLoadFriendListCompleted();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}