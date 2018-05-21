package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.GroupConversationContract;
import com.wang.social.im.mvp.model.GroupConversationModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-15 13:50
 * ============================================
 */
@Module
public class GroupConversationModule {

    GroupConversationContract.View view;

    public GroupConversationModule(GroupConversationContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GroupConversationContract.View provideView(){
        return view;
    }

    @ActivityScope
    @Provides
    GroupConversationContract.Model provideModel(GroupConversationModel model){
        return model;
    }
}
