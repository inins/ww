package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.CTSocialListContract;
import com.wang.social.im.mvp.model.CTSocialListModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-06-14 10:13
 * ============================================
 */
@Module
public class CTSocialListModule {

    CTSocialListContract.View view;

    public CTSocialListModule(CTSocialListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CTSocialListContract.View provideView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CTSocialListContract.Model provideModel(CTSocialListModel model) {
        return model;
    }
}
