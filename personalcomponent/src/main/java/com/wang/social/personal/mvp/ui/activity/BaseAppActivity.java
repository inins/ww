package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.frame.base.BaseActivity;
import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.frame.mvp.BasePresenter;
import com.wang.social.personal.mvp.ui.dialog.DialogLoading;

import java.lang.ref.WeakReference;

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
