package com.frame.component.ui.base;

import android.content.Context;
import android.view.View;

import com.frame.entities.EventBean;
import com.frame.mvp.IView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseController {

    private View root;
    protected Unbinder mUnbinder;

    public BaseController(View root) {
        this.root = root;
        mUnbinder = ButterKnife.bind(this, root);

//        onInitCtrl();
//        onInitData();
    }

    public void registEventBus() {
        EventBus.getDefault().register(this);
    }

    private void unRegistEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
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

    public View getRoot() {
        return root;
    }

    public Context getContext() {
        return root.getContext();
    }

    public IView getIView() {
        return (getContext() instanceof IView) ? (IView) getContext() : null;
    }

    ////////////////////////////////////////

    protected abstract void onInitCtrl();

    protected abstract void onInitData();
}
