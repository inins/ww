package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.wang.social.im.mvp.contract.SocialHomeContract;
import com.wang.social.im.mvp.model.entities.SocialInfo;

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
     * @param socialId
     */
    public void getSocialInfo(String socialId) {
        mApiHelper.execute(mRootView, mModel.getSocialInfo(socialId),
                new ErrorHandleSubscriber<SocialInfo>() {
                    @Override
                    public void onNext(SocialInfo socialInfo) {

                        //获取成员列表
                        getMemberList(socialId);
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