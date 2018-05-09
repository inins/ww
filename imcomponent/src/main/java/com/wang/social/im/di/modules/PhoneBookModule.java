package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.PhoneBookContract;
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
public class PhoneBookModule {

    PhoneBookContract.View view;

    public PhoneBookModule(PhoneBookContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PhoneBookContract.View providePhotoBookView() {
        return view;
    }

    @ActivityScope
    @Provides
    PhoneBookContract.Model providePhotoBookModel(PhoneBookModel model) {
        return model;
    }
}