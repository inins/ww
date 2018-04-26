package com.frame.component.ui.acticity.BGMList;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = BGMListModule.class, dependencies = AppComponent.class)
public interface BGMListComponent {
    void inject(BGMListActivity bgmListActivity);
}
