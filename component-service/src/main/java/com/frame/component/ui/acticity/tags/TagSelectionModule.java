package com.frame.component.ui.acticity.tags;

import com.frame.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;

@Module
public class TagSelectionModule {

    private TagSelectionContract.View view;

    public TagSelectionModule(TagSelectionContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    TagSelectionContract.View provideTagSelectionView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    TagSelectionContract.Model provideTagSelectionModel(TagSelectionModel model) {
        return model;
    }
}
