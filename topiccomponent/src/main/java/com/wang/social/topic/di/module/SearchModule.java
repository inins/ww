package com.wang.social.topic.di.module;

import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.wang.social.topic.mvp.contract.SearchContract;
import com.wang.social.topic.mvp.model.SearchModel;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchModule {

    private SearchContract.View view;

    public SearchModule(SearchContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SearchContract.View provideSearchView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SearchContract.Model provideSearchModel(SearchModel model) {
        return model;
    }
}
