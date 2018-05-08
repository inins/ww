package com.wang.social.im.di.modules;

import com.frame.di.scope.FragmentScope;
import com.wang.social.im.mvp.contract.ConversationListContract;
import com.wang.social.im.mvp.contract.FriendsContract;
import com.wang.social.im.mvp.model.ConversationListModel;
import com.wang.social.im.mvp.model.FriendsModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * {@link com.wang.social.im.mvp.ui.ConversationListFragment} 实例提供者
 * <p>
 * Create by ChenJing on 2018-04-16 20:10
 * ============================================
 */
@Module
public class FriendsModule {

    private FriendsContract.View view;

    public FriendsModule(FriendsContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    FriendsContract.View provideFriendsView() {
        return view;
    }

    @FragmentScope
    @Provides
    FriendsContract.Model provideFriendsModel(FriendsModel model) {
        return model;
    }
}
