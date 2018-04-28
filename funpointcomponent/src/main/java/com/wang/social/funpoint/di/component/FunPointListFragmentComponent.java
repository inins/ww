package com.wang.social.funpoint.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.FragmentScope;
import com.wang.social.funpoint.di.module.FunpointListModule;
import com.wang.social.funpoint.mvp.ui.fragment.FunPointFragment;

import dagger.Component;

/**
 * Created by Bo on 2018-03-27.
 */
@FragmentScope
@Component(modules = FunpointListModule.class, dependencies = AppComponent.class)
public interface FunPointListFragmentComponent {

    void inject(FunPointFragment funShowFragment);
}
