package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.GroupConversationContract;
import com.wang.social.im.mvp.model.entities.ListData;
import com.wang.social.im.mvp.model.entities.TeamInfo;

import javax.inject.Inject;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-15 13:49
 * ============================================
 */
@ActivityScope
public class GroupConversationPresenter extends BasePresenter<GroupConversationContract.Model, GroupConversationContract.View> {

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public GroupConversationPresenter(GroupConversationContract.Model model, GroupConversationContract.View view) {
        super(model, view);
    }

    /**
     * 获取趣聊下所有觅聊
     *
     * @param socialId
     */
    public void getMiList(String socialId) {
        mApiHelper.execute(mRootView, mModel.getTeamList(socialId),
                new ErrorHandleSubscriber<ListData<TeamInfo>>() {
                    @Override
                    public void onNext(ListData<TeamInfo> teamInfoListData) {
                        mRootView.showAllTeams(teamInfoListData.getList());
                    }
                });
    }

    /**
     * 获取趣聊下加入的觅聊
     *
     * @param socialId
     */
    public void getSelfMiList(String socialId) {
        mApiHelper.execute(mRootView, mModel.getSelfTeamList(socialId),
                new ErrorHandleSubscriber<ListData<TeamInfo>>() {
                    @Override
                    public void onNext(ListData<TeamInfo> teamInfoListData) {
                        mRootView.showSelfTeams(teamInfoListData.getList());
                    }
                });
    }
}