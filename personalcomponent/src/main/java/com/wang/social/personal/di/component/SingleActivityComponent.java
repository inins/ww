package com.wang.social.personal.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.personal.di.module.MeDetailModule;
import com.wang.social.personal.di.module.UserModule;
import com.wang.social.personal.mvp.ui.activity.AccountActivity;
import com.wang.social.personal.mvp.ui.activity.AccountExchangeActivity;
import com.wang.social.personal.mvp.ui.activity.FeedbackActivity;
import com.wang.social.personal.mvp.ui.activity.LableActivity;
import com.wang.social.personal.mvp.ui.activity.MeDetailActivity;
import com.wang.social.personal.mvp.ui.activity.MePhotoActivity;
import com.wang.social.personal.mvp.ui.activity.OfficialPhotoActivity;
import com.wang.social.personal.mvp.ui.activity.QrcodeActivity;

import dagger.Component;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:19
 * ========================================
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface SingleActivityComponent {
    void inject(AccountActivity activity);
    void inject(MePhotoActivity activity);
    void inject(FeedbackActivity activity);
    void inject(AccountExchangeActivity activity);
    void inject(OfficialPhotoActivity activity);
    void inject(QrcodeActivity activity);
}