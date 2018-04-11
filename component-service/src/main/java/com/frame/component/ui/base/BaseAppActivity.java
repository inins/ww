package com.frame.component.ui.base;

import android.support.annotation.Nullable;

import com.frame.mvp.BasePresenter;

import javax.inject.Inject;


/**
 * Created by liaoinstan on 2016/7/1 0001.
 */
public abstract class BaseAppActivity<P extends BasePresenter> extends BasicAppActivity {

    @Inject
    @Nullable
    protected P mPresenter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        this.mPresenter = null;
    }
}
