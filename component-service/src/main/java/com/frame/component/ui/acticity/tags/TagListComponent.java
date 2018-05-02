package com.frame.component.ui.acticity.tags;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(modules = TagListModule.class, dependencies = AppComponent.class)
public interface TagListComponent {
    void inject(TagListFragment tagListFragment);
}
