package com.wang.social.login.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.wang.social.login.R;
import com.wang.social.login.R2;
import com.wang.social.login.di.component.DaggerLoginComponent;
import com.wang.social.login.di.module.LoginModule;
import com.wang.social.login.mvp.contract.LoginContract;
import com.wang.social.login.mvp.presenter.LoginPresenter;
import com.frame.base.BaseActivity;
import com.frame.di.component.AppComponent;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.router.facade.annotation.RouteNode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

@RouteNode(path = "/login", desc = "登陆页")
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R2.id.mobile)
    EditText mobile;
    @BindView(R2.id.password)
    EditText password;
    @BindView(R2.id.progress)
    ProgressBar progress;

    @Inject
    RxErrorHandler rxErrorHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.login_activity_login_test;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {

    }

    @Override
    public void showLoading() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void gotoHome() {

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @OnClick(R2.id.login)
    public void onViewClicked() {
        mPresenter.login(mobile.getText().toString(), password.getText().toString());
    }
}
