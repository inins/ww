package com.frame.component.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.frame.base.BasicFragment;
import com.frame.di.component.AppComponent;
import com.frame.mvp.IView;

/**
 * 懒加载fragment鸡肋
 * 只在第一次可见时加载数据(lazyload)
 */
public abstract class BasicLazyFragment extends BasicFragment {

    //fragment 是否可见
    private boolean isVisible;
    //fragment 是否已经执行过懒加载动作
    private boolean isLoad;
    //fragment 是否已经准备好视图
    private boolean isViewCreated;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        //如果可以执行加载动作并且视图已经准备好，则进行懒加载
        if (checklazyload() && isViewCreated) {
            isLoad = true;
            initDataLazy();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
    }

    @Override
    public final void initData(@Nullable Bundle savedInstanceState) {
        //检查状态并执行懒加载动作
        if (checklazyload()) {
            isLoad = true;
            initDataLazy();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
    }

    //检查fragment是否可以执行懒加载动作（如果没有加载过，并且可见，则可以执行）
    private boolean checklazyload() {
        return !isLoad && isVisible;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isLoad = false;
    }

    //懒加载
    protected abstract void initDataLazy();
}
