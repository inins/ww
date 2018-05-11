package com.frame.component.ui.acticity.PersonalCard.di;

import com.frame.component.ui.acticity.PersonalCard.contract.PersonalCardContract;
import com.frame.component.ui.acticity.PersonalCard.model.PersonalCardModel;
import com.frame.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class PersonalCardModule {

    private PersonalCardContract.View view;

    public PersonalCardModule(PersonalCardContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PersonalCardContract.View provideTopicView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PersonalCardContract.Model provideTopicModel(PersonalCardModel model) {
        return model;
    }
}
