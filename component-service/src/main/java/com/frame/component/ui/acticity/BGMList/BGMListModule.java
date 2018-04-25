package com.frame.component.ui.acticity.BGMList;

import com.frame.di.scope.ActivityScope;
import com.frame.component.ui.acticity.BGMList.BGMListContract;
import com.frame.component.ui.acticity.BGMList.BGMListModel;

import dagger.Module;
import dagger.Provides;

@Module
public class BGMListModule {

    private BGMListContract.View view;

    public BGMListModule(BGMListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    BGMListContract.View provideBGMListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    BGMListContract.Model provideBGMListModel(BGMListModel model) {
        return model;
    }
}
