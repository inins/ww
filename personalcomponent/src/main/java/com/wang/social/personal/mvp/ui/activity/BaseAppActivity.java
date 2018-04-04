package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.frame.base.BaseActivity;
import com.frame.di.component.AppComponent;
import com.frame.mvp.BasePresenter;
import com.wang.social.personal.mvp.ui.dialog.DialogLoading;

import java.lang.ref.WeakReference;


/**
 * Created by liaoinstan on 2016/7/1 0001.
 */
public abstract class  BaseAppActivity<P extends BasePresenter> extends BaseActivity<P> {

    private WeakReference<DialogLoading> dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dialogLoading = new WeakReference(new DialogLoading(this));
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogLoading.get() != null) dialogLoading.get().dismiss();
    }


    public final void showLoadingDialog() {
        if (dialogLoading.get() == null) dialogLoading = new WeakReference(new DialogLoading(this));
        dialogLoading.get().show();
    }

    public final void dismissLoadingDialog() {
        if (dialogLoading.get() != null) dialogLoading.get().dismiss();
    }

    public final void hideLoadingDialog() {
        if (dialogLoading.get() != null) dialogLoading.get().hide();
    }
}
