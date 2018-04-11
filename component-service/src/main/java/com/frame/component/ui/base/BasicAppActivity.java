package com.frame.component.ui.base;

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

    private WeakReference<DialogLoading> dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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