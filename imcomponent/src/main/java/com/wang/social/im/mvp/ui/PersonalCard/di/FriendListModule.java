package com.wang.social.im.mvp.ui.PersonalCard.di;

import com.frame.di.scope.FragmentScope;
import com.wang.social.im.mvp.ui.PersonalCard.contract.FriendListContract;
import com.wang.social.im.mvp.ui.PersonalCard.model.FriendListModel;

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
