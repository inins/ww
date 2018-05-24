package com.frame.component.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.frame.base.BaseFragment;
import com.frame.base.BasicFragment;
import com.frame.di.component.AppComponent;
import com.frame.mvp.BasePresenter;
import com.frame.mvp.IView;

import javax.inject.Inject;

public abstract class BaseLazyFragment<P extends BasePresenter> extends BasicLazyFragment implements IView {

    @Inject
    @Nullable
    protected P mPresenter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.onDestroy();
        }
        mPresenter = null;
    }
}
