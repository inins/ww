package com.frame.component.ui.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.frame.base.BasicActivity;
import com.frame.component.ui.dialog.DialogLoading;
import com.frame.component.utils.viewutils.ViewUtil;
import com.frame.di.component.AppComponent;
import com.frame.mvp.IView;

import java.lang.ref.WeakReference;


/**
 * Created by liaoinstan on
 * 不需要依赖注入的BasicAppActivity，子类不需要再实现下面3个方法
 */
public abstract class BasicAppNoDiActivity extends BasicAppActivity implements IView {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }
}
