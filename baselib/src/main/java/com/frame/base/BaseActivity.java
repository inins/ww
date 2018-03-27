package com.frame.base;

import android.support.annotation.Nullable;

import com.frame.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * ======================================
 * Activity 基类
 * 因为Java只能单继承，所以如果要用到继承特定{@link android.app.Activity} 的三方库，那就需要自定义{@link android.app.Activity}
 * 继承于这个特定的{@link android.app.Activity}，然后再按照{@link BaseActivity} 的格式，将代码复制过去，一定要实现{@link com.frame.base.delegate.IActivity}
 * <p>
 * Create by ChenJing on 2018-03-12 15:08
 * ======================================
 */

public abstract class BaseActivity<P extends BasePresenter> extends BasicActivity{

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