package com.wang.social.topic.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.TemplateContract;
import com.wang.social.topic.mvp.model.entities.Template;
import com.wang.social.topic.mvp.model.entities.Templates;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@ActivityScope
public class TemplatePresenter extends
        BasePresenter<TemplateContract.Model, TemplateContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    List<Template> mTemplates = new ArrayList<>();
    // 当前选中的模板,默认模板id -1
    private int mCurrentTemplateId = -1;
    private Template mCurrentTemplate;

    @Inject
    public TemplatePresenter(TemplateContract.Model model, TemplateContract.View view) {
        super(model, view);
    }

    private void initTemplateList() {
        mTemplates.clear();

        // 添加默认
        Template t = new Template();
        t.setId(-1);
        t.setName(mRootView.getDefaultName());
        mTemplates.add(t);
    }

    public boolean isCurrentTemplate(int id) {
        return id == mCurrentTemplateId;
    }

    public List<Template> getTemplates() {
        return mTemplates;
    }

    public void setCurrentTemplate(Template template) {
        this.mCurrentTemplate = template;
    }

    public Template getCurrentTemplate() {
        return mCurrentTemplate;
    }

    public boolean setCurrentTemplateId(int id) {
        if (id != mCurrentTemplateId) {
            mCurrentTemplateId = id;
            mRootView.onNotifyTemplatesChanged();

            return true;
        }

        return false;
    }

    public int getCurrentTemplateId() {
        return mCurrentTemplateId;
    }

    public void loadTemplateList() {
        mApiHelper.execute(mRootView,
                mModel.templeList(1, Integer.MAX_VALUE, 0),
                new ErrorHandleSubscriber<Templates>(mErrorHandler) {
                    @Override
                    public void onNext(Templates templates) {
                        mTemplates.addAll(templates.getList());
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 初始化列表，清空，添加默认
                        initTemplateList();

                        mRootView.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.onTemplateListLoaded();

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