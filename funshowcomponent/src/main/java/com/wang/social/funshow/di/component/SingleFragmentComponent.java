package com.wang.social.funshow.di.component;

import com.frame.di.component.AppComponent;
import com.frame.di.scope.FragmentScope;
import com.wang.social.funshow.mvp.ui.fragment.FunShowFragment;

import dagger.Component;

/**
 * Created by Bo on 2018-03-27.
 */
@FragmentScope
@Component(dependencies = AppComponent.class)
public interface SingleFragmentComponent {
    void inject(FunShowFragment fragment);
}
