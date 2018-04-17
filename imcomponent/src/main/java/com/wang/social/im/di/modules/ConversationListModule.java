package com.wang.social.im.di.modules;

import com.frame.di.scope.FragmentScope;
import com.wang.social.im.mvp.contract.ConversationListContract;
import com.wang.social.im.mvp.model.ConversationListModel;

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
public class ConversationListModule {

    private ConversationListContract.View view;

    public ConversationListModule(ConversationListContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    ConversationListContract.View provideConversationListModel() {
        return view;
    }

    @FragmentScope
    @Provides
    ConversationListContract.Model provideConversationListPresenter(ConversationListModel model) {
        return model;
    }
}
