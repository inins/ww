package com.frame.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import com.frame.integration.IRepositoryManager;
import com.frame.utils.Preconditions;

/**
 * ======================================
 * Model 基类
 * <p>
 * Create by ChenJing on 2018-03-12 14:30
 * ======================================
 */

public class BaseModel implements IModel, LifecycleObserver{

    protected IRepositoryManager mRepositoryManager;

    public BaseModel(IRepositoryManager repositoryManager) {
        Preconditions.checkNotNull(repositoryManager, "%s cannot be null!", IRepositoryManager.class.getName());
        this.mRepositoryManager = repositoryManager;
    }

    @Override
    public void onDestroy() {
        mRepositoryManager = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner){
        owner.getLifecycle().removeObserver(this);
    }
}
