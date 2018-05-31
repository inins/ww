package com.wang.social.im.mvp.presenter;

import com.frame.component.app.Constant;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.view.moneytree.PayHelper;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.mvp.contract.GroupConversationContract;
import com.wang.social.im.mvp.model.entities.GroupJoinCheckResult;
import com.wang.social.im.mvp.model.entities.JoinGroupResult;
import com.wang.social.im.mvp.model.entities.ListData;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.SocialInfo;
import com.wang.social.im.mvp.model.entities.TeamInfo;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

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

    /**
     * 检查加入状态
     *
     * @param groupId
     */
    public void checkJoinStatus(String socialId, String groupId) {
        mApiHelper.execute(mRootView, mModel.checkJoinGroupStatus(groupId),
                new ErrorHandleSubscriber<GroupJoinCheckResult>() {
                    @Override
                    public void onNext(GroupJoinCheckResult groupJoinCheckResult) {
                        if (groupJoinCheckResult.isNeedPay()) {
                            mRootView.showPayDialog(groupJoinCheckResult);
                        } else {
                            joinGroup(socialId, groupJoinCheckResult.getApplyId(), groupJoinCheckResult.isValidation());
                        }
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
     * 加入支付
     *
     * @param socialId
     * @param checkResult
     */
    public void payForJoin(String socialId, GroupJoinCheckResult checkResult) {
        NetPayStoneHelper.newInstance().stonePay(mRootView, checkResult.getJoinCost(), Constant.PAY_OBJECT_TYPE_ADD_GROUP, checkResult.getApplyId(), new NetPayStoneHelper.OnStonePayCallback() {
            @Override
            public void success() {
                joinGroup(socialId, checkResult.getApplyId(), checkResult.isValidation());
            }
        });
    }

    /**
     * 加入觅聊
     *
     * @param socialId
     * @param applyId
     */
    public void joinGroup(String socialId, String applyId, boolean isValidation) {
        mApiHelper.execute(mRootView, mModel.joinGroup(applyId),
                new ErrorHandleSubscriber<JoinGroupResult>() {
                    @Override
                    public void onNext(JoinGroupResult joinGroupResult) {
                        if (!isValidation) {
                            getMiList(socialId);
                            getSelfMiList(socialId);
                        } else {
                            ToastUtil.showToastShort("申请成功");
                        }
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
     * 获取趣聊资料
     *
     * @param socialId
     */
    public void getSocialInfo(String socialId) {
        mApiHelper.execute(mRootView, mModel.getSocialInfo(socialId),
                new ErrorHandleSubscriber<SocialInfo>() {
                    @Override
                    public void onNext(SocialInfo socialInfo) {
                        if (socialInfo.isCreateTeam() || socialInfo.getMemberInfo().getRole() == MemberInfo.ROLE_MASTER) {
                            mRootView.showCreateMi();
                        }
                    }
                });
    }
}