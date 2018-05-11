package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.MemberListContract;
import com.wang.social.im.mvp.model.MemberListModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 20:05
 * ============================================
 */
@Module
public class MemberListModule {

    private MemberListContract.View view;

    public MemberListModule(MemberListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MemberListContract.View provideView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MemberListContract.Model provideModel(MemberListModel model) {
        return model;
    }
}
