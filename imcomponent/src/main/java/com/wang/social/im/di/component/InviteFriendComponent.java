package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.InviteFriendModule;
import com.wang.social.im.mvp.ui.InviteFriendActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-12 14:55
 * ============================================
 */
@ActivityScope
@Component(modules = InviteFriendModule.class, dependencies = AppComponent.class)
public interface InviteFriendComponent {

    void inject(InviteFriendActivity activity);
}
