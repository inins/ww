package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.EnvelopDetailContract;
import com.wang.social.im.mvp.model.EnvelopDetailModel;

import dagger.Module;
import dagger.Provides;

/**
 * ============================================
 * 发送红包相关实例提供
 * <p>
 * Create by ChenJing on 2018-04-24 11:06
 * ============================================
 */
@Module
public class EnvelopDetailModule {

    private EnvelopDetailContract.View view;

    public EnvelopDetailModule(EnvelopDetailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    EnvelopDetailContract.View provideCreateEnvelopView() {
        return view;
    }

    @ActivityScope
    @Provides
    EnvelopDetailContract.Model provideCreateEnvelopModel(EnvelopDetailModel model) {
        return model;
    }
}
