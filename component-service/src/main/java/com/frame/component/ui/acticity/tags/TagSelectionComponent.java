package com.frame.component.ui.acticity.tags;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = TagSelectionModule.class, dependencies = AppComponent.class)
public interface TagSelectionComponent {
    void inject(TagSelectionActivity TagSelectionActivity);
}
