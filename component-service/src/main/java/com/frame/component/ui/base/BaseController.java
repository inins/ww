package com.frame.component.ui.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.frame.component.service.R;
import com.frame.component.view.LoadingLayout;
import com.frame.entities.EventBean;
import com.frame.mvp.IView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseController {

    private View root;
    private IView iView;
    protected Unbinder mUnbinder;

    public BaseController(View root) {
        this(null, root);
    }

    public BaseController(IView iView, View root) {
        this.root = root;
        this.iView = iView;
        mUnbinder = ButterKnife.bind(this, root);
    }

    //########################################
    //################通用方法相关############
    //########################################

    public View getRoot() {
        return root;
    }

    public Context getContext() {
        return root.getContext();
    }

    public IView getIView() {
        if (iView != null) {
            return iView;
        } else {
            return (getContext() instanceof IView) ? (IView) getContext() : null;
        }
    }

    //########################################
    //################eventBus相关############
    //########################################

    public void registEventBus() {
        EventBus.getDefault().register(this);
    }

    private void unRegistEventBus() {
        //事实证明，这个判断没什么卵用，unregister已经检查了注册状态
        //if (EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().unregister(this);
    }

    //默认的eventbus事件处理方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonEvent(EventBean event) {
    }

    //释放资源（在宿主不再使用controller时调用以释放资源）
    public void onDestory() {
        mUnbinder.unbind();
        unRegistEventBus();
    }

    //########################################
    //################加载布局相关############
    //########################################

    private LoadingLayout loadingLayout;

    protected void addLoadingLayout() {
        loadingLayout = new LoadingLayout(getContext());
        loadingLayout.setLayoutParams(getRoot().getLayoutParams());
        //loadingLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        loadingLayout.setLoadingViewSrc(R.layout.layout_loading);
        loadingLayout.setFailViewSrc(R.layout.layout_fail);
        loadingLayout.setLackViewSrc(R.layout.layout_lack);
        ViewGroup parent = (ViewGroup) getRoot().getParent();
        int index = -1;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child == getRoot()) index = i;
        }
        parent.addView(loadingLayout, index);
        parent.removeView(getRoot());
        loadingLayout.addView(getRoot());
    }

    public LoadingLayout getLoadingLayout() {
        return loadingLayout;
    }

    //########################################
    //################     接口   ############
    //########################################

    protected abstract void onInitCtrl();

    protected abstract void onInitData();
}
