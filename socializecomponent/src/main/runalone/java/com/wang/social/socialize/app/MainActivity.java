package com.wang.social.socialize.app;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.socialize.R;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by King on 2018/4/2.
 */

public class MainActivity extends BasicActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.socialize_activity_main;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != compositeDisposable) {
            compositeDisposable.dispose();
        }
    }
}
