package com.frame.component.ui.acticity.tags;

import com.frame.di.scope.FragmentScope;
import com.frame.component.ui.acticity.tags.TagListContract;
import com.frame.component.ui.acticity.tags.TagListModel;

import dagger.Module;
import dagger.Provides;

@Module
public class TagListModule {

    private TagListContract.View view;

    public TagListModule(TagListContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    TagListContract.View provideTagListView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    TagListContract.Model provideTagListModel(TagListModel model) {
        return model;
    }
}
