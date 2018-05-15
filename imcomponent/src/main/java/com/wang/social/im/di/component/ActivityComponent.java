package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.ui.NewUsersActivity;
import com.wang.social.im.mvp.ui.ShareListActivity;
import com.wang.social.im.mvp.ui.SocialChargeSettingActivity;
import com.wang.social.im.mvp.ui.PrivateConversationActivity;
import com.wang.social.im.mvp.ui.SocialLimitActivity;
import com.wang.social.im.mvp.ui.SocialListActivity;
import com.wang.social.im.mvp.ui.TeamChargeSettingActivity;

import dagger.Component;

/**
 * ============================================
 *
 * @link android.app.Activity} 通用Component
 * <p>
 * Create by ChenJing on 2018-04-23 11:19
 * ============================================
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent {

    void inject(PrivateConversationActivity activity);

    void inject(SocialChargeSettingActivity activity);

    void inject(TeamChargeSettingActivity activity);

    void inject(NewUsersActivity activity);

    void inject(SocialLimitActivity activity);

    void inject(ShareListActivity shareListActivity);
}