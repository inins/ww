package com.wang.social.im.mvp.ui.PersonalCard.presenter;

import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.PageList;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.ui.PersonalCard.contract.FriendListContract;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.FriendList;
import com.frame.component.entities.PersonalInfo;

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
    private List<PersonalInfo> mUserInfoList = new ArrayList<>();

    @Inject
    public FriendListPresenter(FriendListContract.Model model, FriendListContract.View view) {
        super(model, view);
    }

    public List<PersonalInfo> getUserInfoList() {
        return mUserInfoList;
    }

    /**
     * 获取用户信息
     * @param userId 用户id
     * @param refresh 是否刷新
     */
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

    /**
     * 聊天列表-搜索已添加的好友
     * @param key 关键字
     * @param phone 手机号
     * @param refresh 是否刷新
     */
    public void searchUser(String key, String phone, boolean refresh) {
        if (refresh) {
            mCurrent = 0;
            mUserInfoList.clear();
        }

        mApiHelper.execute(mRootView,
                mModel.searchUser(key, phone, mCurrent + 1, mSize),
        new ErrorHandleSubscriber<PageList<PersonalInfo>>() {
                    @Override
                    public void onNext(PageList<PersonalInfo> list) {
                        if (null != list && null != list.getList()) {
                            mUserInfoList.addAll(list.getList());
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