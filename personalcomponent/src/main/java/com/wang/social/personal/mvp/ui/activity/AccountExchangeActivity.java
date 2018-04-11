package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.frame.di.component.AppComponent;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.RxLifecycleUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.di.component.DaggerSingleActivityComponent;
import com.wang.social.personal.mvp.entities.AccountBalance;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.ui.dialog.DialogNoticeStone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
