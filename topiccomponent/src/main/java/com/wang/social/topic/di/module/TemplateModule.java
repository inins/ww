package com.wang.social.topic.di.module;

import com.frame.di.scope.ActivityScope;
import com.wang.social.topic.mvp.contract.TemplateContract;
import com.wang.social.topic.mvp.model.TemplateModel;

import dagger.Module;
import dagger.Provides;

@Module
public class TemplateModule {

    private TemplateContract.View view;

    public TemplateModule(TemplateContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    TemplateContract.View provideTemplateView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    TemplateContract.Model provideTemplateModel(TemplateModel model) {
        return model;
    }
}
