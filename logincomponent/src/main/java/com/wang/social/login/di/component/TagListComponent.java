package com.wang.social.login.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.login.di.module.TagListModule;
import com.wang.social.login.mvp.ui.widget.TagListFragment;

import dagger.Component;

@ActivityScope
@Component(modules = TagListModule.class, dependencies = AppComponent.class)
public interface TagListComponent {
    void inject(TagListFragment tagListFragment);
}
