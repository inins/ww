package com.frame.component.ui.acticity.PersonalCard.di;

import com.frame.component.ui.acticity.PersonalCard.contract.FriendListContract;
import com.frame.component.ui.acticity.PersonalCard.model.FriendListModel;
import com.frame.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class FriendListModule {

    private FriendListContract.View view;

    public FriendListModule(FriendListContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    FriendListContract.View provideFriendListView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    FriendListContract.Model provideFriendListModel(FriendListModel model) {
        return model;
    }
}
