package com.wang.social.im.di.modules;

import com.frame.di.scope.FragmentScope;
import com.wang.social.im.mvp.contract.NobodyContract;
import com.wang.social.im.mvp.model.NobodyModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-16 9:37
 * ============================================
 */
@Module
public class NobodyModule {

    NobodyContract.View view;

    public NobodyModule(NobodyContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    NobodyContract.View provideView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    NobodyContract.Model provideModel(NobodyModel model) {
        return model;
    }
}
