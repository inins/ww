package com.wang.social.im.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.FragmentScope;
import com.wang.social.im.mvp.ui.fragments.ContactsFragment;
import com.wang.social.im.mvp.ui.fragments.NobodyFragment;
import com.wang.social.im.mvp.ui.fragments.SocialConversationFragment;
import com.wang.social.im.mvp.ui.fragments.TeamConversationFragment;

import dagger.Component;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-16 9:32
 * ============================================
 */
@FragmentScope
@Component(dependencies = AppComponent.class)
public interface FragmentComponent {

    void inject(SocialConversationFragment fragment);

    void inject(TeamConversationFragment fragment);

    void inject(ContactsFragment fragment);
}