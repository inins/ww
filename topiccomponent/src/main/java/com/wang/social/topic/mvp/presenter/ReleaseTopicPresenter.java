package com.wang.social.topic.mvp.presenter;

import com.frame.component.common.NetParam;
import com.frame.component.ui.acticity.BGMList.Music;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.ReleaseTopicContract;
import com.wang.social.topic.mvp.model.entities.Template;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@ActivityScope
public class ReleaseTopicPresenter extends
        BasePresenter<ReleaseTopicContract.Model, ReleaseTopicContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    private Music mBGMusic;
    private Template mTemplate;

    private Map<String, Object> mNetParam = new LinkedHashMap<>();

    public ReleaseTopicPresenter resetNetParam() {
        mNetParam.clear();
        return this;
    }

    public ReleaseTopicPresenter setInfoUserIds(String informUserIds) {
        mNetParam.put("informUserIds", informUserIds);
        return this;
    }

    public ReleaseTopicPresenter setBackgroundMusicId() {
        if (null != mBGMusic) {
            mNetParam.put("backgroundMusicId", mBGMusic.getMusicId());
        }
        return this;
    }

    public ReleaseTopicPresenter setBackgroundImage(String backgroundImage) {
        mNetParam.put("backgroundImage", backgroundImage);
        return this;
    }

    public ReleaseTopicPresenter setTemplateId() {
        if (null != mTemplate) {
            mNetParam.put("templateId", mTemplate.getId());
        }
        return this;
    }

    public ReleaseTopicPresenter setAddress(String address) {
        mNetParam.put("address", address);
        return this;
    }

    public ReleaseTopicPresenter setAdcode(String adcode) {
        mNetParam.put("adcode", adcode);
        return this;
    }

    public ReleaseTopicPresenter setLatitude(String latitude) {
        mNetParam.put("latitude", latitude);
        return this;
    }

    public ReleaseTopicPresenter setLongitude(String longitude) {
        mNetParam.put("longitude", longitude);
        return this;
    }

    public ReleaseTopicPresenter setGemston(int gemstone) {
        mNetParam.put("gemstone", gemstone);
        return this;
    }

    public ReleaseTopicPresenter setRelateState(int relateState) {
        mNetParam.put("relateState", relateState);
        return this;
    }

    public ReleaseTopicPresenter setTitle(String title) {
        mNetParam.put("title", title);
        return this;
    }

    public ReleaseTopicPresenter setContent(String content) {
        mNetParam.put("content", content);
        return this;
    }

    public ReleaseTopicPresenter setFirstStrff(String firstStrff) {
        mNetParam.put("firstStrff", firstStrff);
        return this;
    }

    public ReleaseTopicPresenter setTagIds(String tagIds) {
        mNetParam.put("tagIds", tagIds);
        return this;
    }

    @Inject
    public ReleaseTopicPresenter(ReleaseTopicContract.Model model, ReleaseTopicContract.View view) {
        super(model, view);
    }

    public Music getBGMusic() {
        return mBGMusic;
    }

    public void setBGMusic(Music BGMusic) {
        mBGMusic = BGMusic;
    }

    public Template getTemplate() {
        return mTemplate;
    }

    public void setTemplate(Template template) {
        mTemplate = template;
    }


    public void addTopic() {
        mApiHelper.executeForData(mRootView,
                mModel.addTopic(mNetParam),
                new ErrorHandleSubscriber(mErrorHandler) {

                    @Override
                    public void onNext(Object o) {
                    }


                    @Override
                    public void onError(Throwable e) {

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}