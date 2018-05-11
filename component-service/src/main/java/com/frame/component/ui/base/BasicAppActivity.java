package com.frame.component.ui.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;

import com.frame.base.BasicActivity;
import com.frame.component.ui.dialog.DialogLoading;
import com.frame.component.utils.viewutils.ViewUtil;

import java.lang.ref.WeakReference;


/**
 * Created by liaoinstan on
 * 默认的对话框弹窗
 * 提供toolbar自动设置返回事件
 */
public abstract class BasicAppActivity extends BasicActivity {

    protected Toolbar toolbar;

    protected WeakReference<DialogLoading> dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //禁止横屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        dialogLoading = new WeakReference(new DialogLoading(this));
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void beforeInitData() {
        toolbar = ViewUtil.findToolbar((ViewGroup) getWindow().getDecorView());
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
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

    private boolean isResume = false;
    @Override
    protected void onResume() {
        super.onResume();

        isResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = false;
        if (dialogLoading.get() != null) dialogLoading.get().dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showLoadingDialog() {
        if (dialogLoading.get() == null) dialogLoading = new WeakReference(new DialogLoading(this));
        if (!dialogLoading.get().isShowing()){
            dialogLoading.get().show();
        }
    }

    public final void dismissLoadingDialog() {
        if (dialogLoading.get() != null) dialogLoading.get().dismiss();
    }

    public final void hideLoadingDialog() {
        if (dialogLoading.get() != null) dialogLoading.get().hide();
    }
}
