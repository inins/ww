package com.frame.component.ui.acticity.PersonalCard.di;

import com.frame.component.ui.acticity.PersonalCard.PersonalCardActivity;
import com.frame.component.ui.acticity.PersonalCard.model.PersonalCardModel;
import com.frame.di.component.AppComponent;
import com.frame.di.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = PersonalCardModule.class, dependencies = AppComponent.class)
public interface PersonalCardComponent {
    void inject(PersonalCardActivity activity);
}
