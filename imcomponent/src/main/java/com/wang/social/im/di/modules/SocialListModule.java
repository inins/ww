package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.SocialListContract;
import com.wang.social.im.mvp.model.SocialListModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 9:57
 * ============================================
 */
@Module
public class SocialListModule {

    SocialListContract.View view;

    public SocialListModule(SocialListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SocialListContract.View provideView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    SocialListContract.Model provideModel(SocialListModel model){
        return model;
    }
}
