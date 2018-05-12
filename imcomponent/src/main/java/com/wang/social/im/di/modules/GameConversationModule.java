package com.wang.social.im.di.modules;

import com.frame.di.scope.FragmentScope;
import com.wang.social.im.mvp.contract.GameConversationContract;
import com.wang.social.im.mvp.model.GameConversationModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-11 11:36
 * ============================================
 */
@Module
public class GameConversationModule {

    private GameConversationContract.View view;

    public GameConversationModule(GameConversationContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    GameConversationContract.View provideView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    GameConversationContract.Model provideModel(GameConversationModel model) {
        return model;
    }
}
