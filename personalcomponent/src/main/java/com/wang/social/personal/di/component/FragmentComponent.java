package com.wang.social.personal.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.FragmentScope;
import com.wang.social.personal.mvp.ui.fragment.PersonalFragment;

import dagger.Component;

/**
 * Created by Bo on 2018-03-27.
 */
@FragmentScope
@Component(dependencies = AppComponent.class)
public interface FragmentComponent {

    void inject(PersonalFragment personalFragment);
}
