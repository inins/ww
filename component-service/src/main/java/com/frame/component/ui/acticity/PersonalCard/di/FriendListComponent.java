package com.frame.component.ui.acticity.PersonalCard.di;

import com.frame.component.ui.acticity.PersonalCard.ui.fragment.FriendListFragment;
import com.frame.di.component.AppComponent;
import com.frame.di.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(modules = FriendListModule.class, dependencies = AppComponent.class)
public interface FriendListComponent {
    void inject(FriendListFragment fragment);
}
