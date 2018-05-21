package com.wang.social.im.mvp.ui.PersonalCard.presenter;

import com.frame.component.helper.QiNiuManager;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.ui.PersonalCard.contract.PersonalCardContract;
import com.frame.component.entities.PersonalInfo;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.UserStatistics;

import javax.inject.Inject;
import timber.log.Timber;


@ActivityScope
public class PersonalCardPresenter extends
        BasePresenter<PersonalCardContract.Model, PersonalCardContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;
    @Inject
    QiNiuManager qiNiuManager;


    @Inject
    public PersonalCardPresenter(PersonalCardContract.Model model, PersonalCardContract.View view) {
        super(model, view);
    }

    /**
     * 用户数据统计（我的/推荐/个人名片）
     *
     * @param userId 用户ID
     */
    public void loadUserStatistics(int userId) {
        mApiHelper.execute(mRootView,
                mModel.getUserStatistics(userId),
                new ErrorHandleSubscriber<UserStatistics>() {
                    @Override
                    public void onNext(UserStatistics userStatistics) {
                        mRootView.onLoadUserStatisticsSuccess(userStatistics);
                    }
                });
    }

    /**
     * 用户信息
     *
     * @param userId 用户id
     */
    public void loadUserInfoAndPhotos(int userId) {
        mApiHelper.execute(mRootView,
                mModel.getUserInfoAndPhotos(userId),
                new ErrorHandleSubscriber<PersonalInfo>() {
                    @Override
                    public void onNext(PersonalInfo userInfo) {
                        mRootView.onLoadUserInfoSuccess(userInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.toastShort(e.getMessage());
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    /**
     * 好友列表-好友设置备注
     * @param friendUserId 好友id
     * @param comment 备注
     */
    public void setFriendRemard(int friendUserId, String comment) {
        mApiHelper.executeForData(mRootView,
                mModel.setFriendComment(friendUserId, comment),
                new ErrorHandleSubscriber() {
                    @Override
                    public void onNext(Object o) {
                        mRootView.onSetFriendRemarkSuccess(comment);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.toastShort(e.getMessage());
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    /**
     * 好友列表-好友设置头像
     * @param friendUserId 好友id
     * @param avatarUrl 备注头像url
     */
    public void setFriendAvatar(int friendUserId, String avatarUrl) {
        if (!avatarUrl.startsWith("http:")) {
            mRootView.showLoading();
            // 上传头像
            qiNiuManager.uploadFile(mRootView, avatarUrl, new QiNiuManager.OnSingleUploadListener() {
                @Override
                public void onSuccess(String url) {
                    Timber.i("上传头像成功 " + url);

                    uploadFriendAvatar(friendUserId, url);
                }

                @Override
                public void onFail() {
                    // 上传附件失败
                    mRootView.hideLoading();
                }
            });
        } else {
            uploadFriendAvatar(friendUserId, avatarUrl);
        }
    }

    private void uploadFriendAvatar(int friendUserId, String avatarUrl) {
        mApiHelper.executeForData(mRootView,
                mModel.setFriendAvatar(friendUserId, avatarUrl),
                new ErrorHandleSubscriber() {
                    @Override
                    public void onNext(Object o) {
                        mRootView.onSetFriendAvatarSuccess(avatarUrl);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.toastShort(e.getMessage());
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    /**
     * 删除好友关系
     *
     * @param friendUserId 用户id
     */
    public void deleteFriend(int friendUserId) {
        mApiHelper.executeForData(mRootView,
                mModel.deleteFriend(friendUserId),
                new ErrorHandleSubscriber() {
                    @Override
                    public void onNext(Object o) {
                        mRootView.onDeleteFriendSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.toastShort(e.getMessage());
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    /**
     * 拉黑或取消拉黑
     * @param blackUserId 用户id
     * @param black 是否拉黑
     */
    public void changeMyBlack(int blackUserId, boolean black) {
        mApiHelper.executeForData(mRootView,
                mModel.changeMyBlack(blackUserId, black),
                new ErrorHandleSubscriber() {
                    @Override
                    public void onNext(Object o) {
                        mRootView.onChangeMyBlackSuccess(!black);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.toastShort(e.getMessage());
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    /**
     * 申请添加好友
     *
     * @param addUserId 用户id
     * @param reason 申请内容
     */
    public void addFriendApply(int addUserId, String reason) {
        mApiHelper.executeForData(mRootView,
                mModel.addFriendApply(addUserId, reason),
                new ErrorHandleSubscriber() {
                    @Override
                    public void onNext(Object o) {
                        mRootView.onAddFriendApplySuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.toastShort(e.getMessage());
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    public void agreeApply(int friendUserId, int msgId) {
        agreeOrRejectAdd(friendUserId, msgId, 0);
    }

    public void rejectApply(int friendUserId, int msgId) {
        agreeOrRejectAdd(friendUserId, msgId, 1);
    }

    /**
     * 同意、拒绝添加好友
     * @param friendUserId 好友id
     * @param msgId 消息id
     * @param type 类型（0：同意，1：拒绝）
     */
    private void agreeOrRejectAdd(int friendUserId, int msgId, int type) {
        mApiHelper.executeForData(mRootView,
                mModel.agreeOrRejectAdd(friendUserId, msgId, type),
                new ErrorHandleSubscriber() {
                    @Override
                    public void onNext(Object o) {
                        mRootView.onAgreeOrRejectApllySuccess(type);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.toastShort(e.getMessage());
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}