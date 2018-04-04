package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.FragmentScope;
import com.wang.social.im.di.modules.ConversationModule;
import com.wang.social.im.mvp.ui.ConversationFragment;

import dagger.Component;

/**
 * ======================================
 * <p>
 * Create by ChenJing on 2018-04-03 16:53
 * ======================================
 */
@FragmentScope
@Component(modules = ConversationModule.class, dependencies = AppComponent.class)
public interface ConversationComponent {

    void inject(ConversationFragment fragment);
}
