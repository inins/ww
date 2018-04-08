package com.wang.social.login.di.module;

import com.frame.di.scope.ActivityScope;
import com.wang.social.login.mvp.contract.TagListContract;
import com.wang.social.login.mvp.model.TagListModel;

import dagger.Module;
import dagger.Provides;

@Module
public class TagListModule {

    private TagListContract.View view;

    public TagListModule(TagListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    TagListContract.View provideTagListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    TagListContract.Model provideTagListModel(TagListModel model) {
        return model;
    }
}
