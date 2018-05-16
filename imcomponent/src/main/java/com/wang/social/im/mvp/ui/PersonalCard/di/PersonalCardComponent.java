package com.wang.social.im.mvp.ui.PersonalCard.di;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.ui.PersonalCard.PersonalCardActivity;

import dagger.Component;

@ActivityScope
@Component(modules = PersonalCardModule.class, dependencies = AppComponent.class)
public interface PersonalCardComponent {
    void inject(PersonalCardActivity activity);
}
