package com.wang.social.im.mvp.ui.PersonalCard.di;

import com.frame.di.scope.ActivityScope;
import com.wang.social.im.mvp.ui.PersonalCard.contract.PersonalCardContract;
import com.wang.social.im.mvp.ui.PersonalCard.model.PersonalCardModel;

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
