package com.wang.social.personal.mvp.ui.activity;

import android.os.Bundle;

import com.frame.base.BaseActivity;
import com.frame.base.BasicActivity;
import com.wang.social.personal.mvp.ui.dialog.DialogLoading;

import java.lang.ref.WeakReference;


/**
 * Created by liaoinstan on 2016/7/1 0001.
 */
public abstract class BasicAppActivity extends BasicActivity {

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
