package com.wang.social.im.di.modules;

import com.frame.di.scope.FragmentScope;
import com.wang.social.im.mvp.contract.ConversationContract;
import com.wang.social.im.mvp.model.ConversationModel;

import dagger.Module;
import dagger.Provides;

/**
 * ======================================
 * <p>
 * Create by ChenJing on 2018-04-03 16:54
 * ======================================
 */
@Module
public class ConversationModule {

    private ConversationContract.View view;

    public ConversationModule(ConversationContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    ConversationContract.View provideConversationView(){
        return view;
    }

    @FragmentScope
    @Provides
    ConversationContract.Model provideConversationModel(ConversationModel model){
        return model;
    }
}
