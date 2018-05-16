package com.wang.social.im.mvp.presenter;

import com.frame.component.entities.UserInfo;
import com.frame.component.view.barview.BarUser;
import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.NobodyContract;
import com.wang.social.im.mvp.model.entities.ListData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-16 9:39
 * ============================================
 */
@FragmentScope
public class NobodyPresenter extends BasePresenter<NobodyContract.Model, NobodyContract.View> {

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public NobodyPresenter(NobodyContract.Model model, NobodyContract.View view) {
        super(model, view);
    }

    public void getKnowledge() {
        mApiHelper.execute(mRootView, mModel.getKnowledgeUser(),
                new ErrorHandleSubscriber<ListData<UserInfo>>() {
                    @Override
                    public void onNext(ListData<UserInfo> userInfoListData) {
                        List<BarUser> barUsers = new ArrayList<>();
                        for (UserInfo userInfo : userInfoListData.getList()) {
                            barUsers.add(new BarUser(userInfo.getPortrait()));
                            if (barUsers.size() >= 3){
                                break;
                            }
                        }
                        mRootView.showKnowledgeUsers(barUsers);
                    }
                });
    }

    public void getFunShow() {
        mApiHelper.execute(mRootView, mModel.getFunShowUser(),
                new ErrorHandleSubscriber<ListData<UserInfo>>() {
                    @Override
                    public void onNext(ListData<UserInfo> userInfoListData) {
                        List<BarUser> barUsers = new ArrayList<>();
                        for (UserInfo userInfo : userInfoListData.getList()) {
                            barUsers.add(new BarUser(userInfo.getPortrait()));
                            if (barUsers.size() >= 3){
                                break;
                            }
                        }
                        mRootView.showFunShowUsers(barUsers);
                    }
                });
    }

    public void getNewUsers() {
        mApiHelper.execute(mRootView, mModel.getNewUsers(),
                new ErrorHandleSubscriber<ListData<UserInfo>>() {
                    @Override
                    public void onNext(ListData<UserInfo> userInfoListData) {
                        List<BarUser> barUsers = new ArrayList<>();
                        for (UserInfo userInfo : userInfoListData.getList()) {
                            barUsers.add(new BarUser(userInfo.getPortrait()));
                            if (barUsers.size() >= 3) {
                                break;
                            }
                        }
                        mRootView.showNewUsers(barUsers);
                    }
                });
    }
}