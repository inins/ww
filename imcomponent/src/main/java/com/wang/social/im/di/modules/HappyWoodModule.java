package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.HappyWoodContract;
import com.wang.social.im.mvp.contract.PhoneBookContract;
import com.wang.social.im.mvp.model.HappyWoodModel;
import com.wang.social.im.mvp.model.PhoneBookModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 15:20
 * ============================================
 */
@Module
public class HappyWoodModule {

    HappyWoodContract.View view;

    public HappyWoodModule(HappyWoodContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HappyWoodContract.View provideView() {
        return view;
    }

    @ActivityScope
    @Provides
    HappyWoodContract.Model provideModel(HappyWoodModel model) {
        return model;
    }
}