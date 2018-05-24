package com.frame.component.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.frame.base.BasicFragment;
import com.frame.di.component.AppComponent;
import com.frame.mvp.IView;

public abstract class BasicLazyFragment extends BasicFragment {

    private boolean isVisible;
    private boolean isLoad;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        lazyload();
    }

    @Override
    public final void initData(@Nullable Bundle savedInstanceState) {
        lazyload();
    }

    private void lazyload() {
        if (!isLoad && isVisible && mRootView != null) {
            isLoad = true;
            initDataLazy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isLoad = false;
    }

    protected abstract void initDataLazy();
}
