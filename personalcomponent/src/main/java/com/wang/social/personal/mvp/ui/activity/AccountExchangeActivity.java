package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.wang.social.personal.R;
import com.wang.social.personal.di.component.DaggerSingleActivityComponent;

import javax.inject.Inject;

public class AccountExchangeActivity extends BasicAppActivity implements IView {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    IRepositoryManager mRepositoryManager;


    public static void start(Context context) {
        Intent intent = new Intent(context, AccountExchangeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_account_exchange;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        netGetAccountData();
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSingleActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    private void netGetAccountData() {
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
