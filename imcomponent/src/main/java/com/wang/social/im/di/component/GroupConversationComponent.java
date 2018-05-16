package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.di.modules.GroupConversationModule;
import com.wang.social.im.mvp.ui.GroupConversationActivity;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-15 13:52
 * ============================================
 */
@ActivityScope
@Component(modules = GroupConversationModule.class, dependencies = AppComponent.class)
public interface GroupConversationComponent {

    void inject(GroupConversationActivity activity);
}