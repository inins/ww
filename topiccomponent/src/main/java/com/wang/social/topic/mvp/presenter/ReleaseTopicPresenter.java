package com.wang.social.topic.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.ReleaseTopicContract;

import javax.inject.Inject;

@ActivityScope
public class ReleaseTopicPresenter extends
        BasePresenter<ReleaseTopicContract.Model, ReleaseTopicContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    int mCurrentTemplateId = -1;
    String mCurrentTemplateUrl;

    @Inject
    public ReleaseTopicPresenter(ReleaseTopicContract.Model model, ReleaseTopicContract.View view) {
        super(model, view);
    }

    public int getCurrentTemplateId() {
        return mCurrentTemplateId;
    }

    public void setCurrentTemplateId(int currentTemplateId) {
        mCurrentTemplateId = currentTemplateId;
    }

    public String getCurrentTemplateUrl() {
        return mCurrentTemplateUrl;
    }

    public void setCurrentTemplateUrl(String currentTemplateUrl) {
        mCurrentTemplateUrl = currentTemplateUrl;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}