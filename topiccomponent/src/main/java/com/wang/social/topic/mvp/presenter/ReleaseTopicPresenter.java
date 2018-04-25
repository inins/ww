package com.wang.social.topic.mvp.presenter;

import com.frame.component.ui.acticity.BGMList.Music;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.ReleaseTopicContract;
import com.wang.social.topic.mvp.model.entities.Template;

import javax.inject.Inject;

@ActivityScope
public class ReleaseTopicPresenter extends
        BasePresenter<ReleaseTopicContract.Model, ReleaseTopicContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    Music mBGMusic;
    Template mTemplate;

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}