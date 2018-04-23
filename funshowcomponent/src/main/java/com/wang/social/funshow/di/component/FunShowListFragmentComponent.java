package com.wang.social.funshow.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.FragmentScope;
import com.wang.social.funshow.di.module.FunshowListModule;
import com.wang.social.funshow.mvp.ui.fragment.FunShowFragment;

import dagger.Component;

/**
 * Created by Bo on 2018-03-27.
 */
@FragmentScope
@Component(modules = FunshowListModule.class, dependencies = AppComponent.class)
public interface FunShowListFragmentComponent {

    void inject(FunShowFragment funShowFragment);
}
