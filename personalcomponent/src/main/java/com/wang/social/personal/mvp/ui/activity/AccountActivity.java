package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.entities.QiNiu;
import com.frame.component.entities.dto.QiNiuDTO;
import com.frame.di.component.AppComponent;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.RxLifecycleUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.di.component.DaggerSingleActivityComponent;
import com.wang.social.personal.mvp.entities.AccountBalance;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.ui.dialog.DialogNoticeStone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AccountActivity extends BasicAppActivity implements IView {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    IRepositoryManager mRepositoryManager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_all)
    TextView textAll;
    @BindView(R.id.text_coulduse)
    TextView textCoulduse;

    public static void start(Context context) {
        Intent intent = new Intent(context, AccountActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_account;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        netGetAccountData();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                AccountDepositDetailActivity.start(this);
                break;
            case R.id.btn_exchange_stone:
                break;
            case R.id.btn_about_stone:
                DialogNoticeStone.newDialog(this).show();
                break;
            case R.id.btn_recharge:
                AccountRechargeActivity.start(this);
                break;
            case R.id.btn_deposit:
                AccountDepositActivity.start(this);
                break;
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
        mRepositoryManager.obtainRetrofitService(UserService.class).accountBalance()
                .subscribeOn(Schedulers.newThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        hideLoading();
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle((IView) AccountActivity.this))
                .subscribe(new ErrorHandleSubscriber<BaseJson<AccountBalance>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<AccountBalance> baseJson) {
                        AccountBalance accountBalance = baseJson.getData();
                        textAll.setText(accountBalance.getAmountDiamond() + "");
                        textCoulduse.setText("可提现钻石：" + accountBalance.getAmount());
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                });
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
