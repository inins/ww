package com.wang.social.im.di.modules;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.contract.CreateEnvelopContract;
import com.wang.social.im.mvp.model.CreateEnvelopModel;

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
public class CreateEnvelopModule {

    private CreateEnvelopContract.View view;

    public CreateEnvelopModule(CreateEnvelopContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CreateEnvelopContract.View provideCreateEnvelopView() {
        return view;
    }

    @ActivityScope
    @Provides
    CreateEnvelopContract.Model provideCreateEnvelopModel(CreateEnvelopModel model) {
        return model;
    }
}
