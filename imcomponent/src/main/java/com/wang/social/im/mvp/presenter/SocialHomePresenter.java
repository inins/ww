package com.wang.social.im.mvp.presenter;

import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.NetShareHelper;
import com.frame.component.helper.QiNiuManager;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiException;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.ToastUtil;
import com.wang.social.im.mvp.contract.SocialHomeContract;
import com.wang.social.im.mvp.model.entities.ListData;
import com.wang.social.im.mvp.model.entities.SocialInfo;
import com.wang.social.im.mvp.model.entities.TeamInfo;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-28 16:46
 * ============================================
 */
@ActivityScope
public class SocialHomePresenter extends GroupPresenter<SocialHomeContract.Model, SocialHomeContract.View> {

    @Inject
    public SocialHomePresenter(SocialHomeContract.Model model, SocialHomeContract.View view) {
        super(model, view);
    }

    /**
     * 获取趣聊信息
     *
     * @param socialId
     */
    public void getSocialInfo(String socialId) {
        mApiHelper.execute(mRootView, mModel.getSocialInfo(socialId),
                new ErrorHandleSubscriber<SocialInfo>() {
                    @Override
                    public void onNext(SocialInfo socialInfo) {

                        mRootView.showSocialInfo(socialInfo);

                        //获取成员列表
                        getMemberList(socialId);
                        //获取觅聊列表
                        getTeamList(socialId);
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
     * 修改信息
     *
     * @param social
     */
    public void updateSocialInfo(SocialInfo social) {
        mApiHelper.executeNone(mRootView, mModel.updateSocialInfo(social),
                new ErrorHandleSubscriber<BaseJson>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson baseJson) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (e instanceof ApiException) {
                            ToastUtil.showToastShort(((ApiException) e).getMsg());
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
     * 修改封面信息
     *
     * @param path
     * @param socialInfo
     */
    public void updateCover(String path, SocialInfo socialInfo) {
        if (!path.startsWith("http")) {
            QiNiuManager manager = new QiNiuManager(mRepositoryManager);
            manager.uploadFile(mRootView, path, new QiNiuManager.OnSingleUploadListener() {
                @Override
                public void onSuccess(String url) {
                    socialInfo.setCover(url);
                    updateSocialInfo(socialInfo);
                }

                @Override
                public void onFail() {
                    ToastUtil.showToastShort("封面上传失败");
                    mRootView.hideLoading();
                }
            });
        } else {
            socialInfo.setCover(path);
            updateSocialInfo(socialInfo);
        }
    }

    /**
     * 获取觅聊列表
     *
     * @param socialId
     */
    public void getTeamList(String socialId) {
        mApiHelper.execute(mRootView, mModel.getTeamList(socialId),
                new ErrorHandleSubscriber<ListData<TeamInfo>>() {
                    @Override
                    public void onNext(ListData<TeamInfo> teamInfoListData) {
                        mRootView.showTeams(teamInfoListData.getList());
                    }
                });
    }

    public void recordShare(String socialId) {
        NetShareHelper.newInstance()
                .netShare(mRootView, AppDataHelper.getUser().getUserId(), null, Integer.valueOf(socialId), NetShareHelper.SHARE_TYPE_GROUP, 0, new NetShareHelper.OnShareCallback() {
                    @Override
                    public void success() {

                    }
                });
    }
}