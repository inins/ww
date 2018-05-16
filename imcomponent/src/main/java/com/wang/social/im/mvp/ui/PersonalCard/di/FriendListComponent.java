package com.wang.social.im.mvp.ui.PersonalCard.di;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.FragmentScope;
import com.wang.social.im.mvp.ui.PersonalCard.ui.fragment.FriendListFragment;

import dagger.Component;

@FragmentScope
@Component(modules = FriendListModule.class, dependencies = AppComponent.class)
public interface FriendListComponent {
    void inject(FriendListFragment fragment);
}
