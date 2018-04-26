package com.wang.social.topic.mvp.presenter;

import android.content.Context;

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
    // 当前选中的模板
    private Template mCurrentTemplate;
    // 原始的，上个页面传过来的
    private Template mOriginalTemplate;

    @Inject
    public TemplatePresenter(TemplateContract.Model model, TemplateContract.View view) {
        super(model, view);
    }

    private void initTemplateList() {
        mTemplates.clear();

        // 添加默认
        mTemplates.add(Template.newDefault(mRootView.getViewContext()));
    }

    /**
     * 是否改变了模板选择
     * @return 是否改变
     */
    public boolean isTemplateChanged() {
        if (null != mOriginalTemplate && null != mCurrentTemplate) {
            return mOriginalTemplate.getId() != mCurrentTemplate.getId();
        } else if (mOriginalTemplate == null) {
            return true;
        } else if (mCurrentTemplate == null) {
            return true;
        }

        return false;
    }

    public Template getOriginalTemplate() {
        return mOriginalTemplate;
    }

    public void setOriginalTemplate(Template originalTemplate) {
        mOriginalTemplate = originalTemplate;
    }

    public boolean isCurrentTemplate(int id) {
        if (mCurrentTemplate != null) {
            return mCurrentTemplate.getId() == id;
        }

        return false;
    }

    public List<Template> getTemplates() {
        return mTemplates;
    }

    public void setCurrentTemplate(Context context, Template template) {
        this.mCurrentTemplate = template == null ? Template.newDefault(context) : template;
    }

    public Template getCurrentTemplate() {
        return mCurrentTemplate;
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