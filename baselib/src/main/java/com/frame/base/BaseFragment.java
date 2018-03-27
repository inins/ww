package com.frame.base;

import android.support.annotation.Nullable;

import com.frame.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * ======================================
 * MVP Fragment 让Fragment拥有MVP特性，并自动注入Presenter
 * <p>
 * Create by ChenJing on 2018-03-12 15:43
 * ======================================
 */

public abstract class BaseFragment<P extends BasePresenter> extends BasicFragment{

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
