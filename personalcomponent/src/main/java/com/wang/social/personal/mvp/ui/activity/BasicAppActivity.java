package com.wang.social.personal.mvp.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ScrollView;
import android.support.v7.widget.Toolbar;

import com.frame.base.BasicActivity;
import com.wang.social.personal.mvp.ui.dialog.DialogLoading;
import com.wang.social.personal.utils.viewutils.ViewUtil;

import java.lang.ref.WeakReference;


/**
 * Created by liaoinstan on 2016/7/1 0001.
 */
public abstract class BasicAppActivity extends BasicActivity {

    private Toolbar toolbar;

    private WeakReference<DialogLoading> dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dialogLoading = new WeakReference(new DialogLoading(this));
        super.onCreate(savedInstanceState);
        toolbar = ViewUtil.findToolbar((ViewGroup) getWindow().getDecorView());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
