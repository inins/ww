package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.InviteFriendContract;
import com.wang.social.im.mvp.model.InviteFriendModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-12 14:53
 * ============================================
 */
@Module
public class InviteFriendModule {

    private InviteFriendContract.View view;

    public InviteFriendModule(InviteFriendContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    InviteFriendContract.View provideView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    InviteFriendContract.Model provideModel(InviteFriendModel model){
        return model;
    }
}
