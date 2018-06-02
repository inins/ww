package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.frame.component.common.AppConstant;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.di.component.DaggerSingleActivityComponent;
import com.wang.social.personal.mvp.entities.AccountBalance;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.ui.dialog.DialogNoticeStone;

import javax.inject.Inject;

import butterknife.BindView;

@RouteNode(path = "/account", desc = "个人账户页面")
public class AccountActivity extends BasicAppActivity implements IView {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    IRepositoryManager mRepositoryManager;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.text_stone_all)
    TextView textStoneAll;
    @BindView(R2.id.text_diamond_all)
    TextView textDiamondAll;
    @BindView(R2.id.text_coulduse)
    TextView textCoulduse;

    private AccountBalance accountBalance;

    public static void start(Context context) {
        Intent intent = new Intent(context, AccountActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_ACCOUNT_EXCHANGE_STONE:
                netGetAccountData();
                break;
            case EventBean.EVENT_ACCOUNT_RECHARGE_SUCCESS:
                netGetAccountData();
                break;
        }
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
        int i = v.getId();
        if (i == R.id.btn_right) {
            AccountDepositDetailActivity.start(this);

        } else if (i == R.id.btn_question) {
            WebActivity.start(this, AppConstant.Url.commonProblem);

        } else if (i == R.id.btn_exchange_stone) {
            AccountExchangeActivity.start(this);

        } else if (i == R.id.btn_about_stone) {
            DialogNoticeStone.newDialog(this).show();

        } else if (i == R.id.btn_recharge) {
            AccountRechargeActivity.start(this);

        } else if (i == R.id.btn_deposit) {
            if (accountBalance != null)
                AccountDepositActivity.start(this, accountBalance.getAmountDiamond(), accountBalance.getAmount());
        }
    }

    private void setAccountData(AccountBalance accountBalance) {
        this.accountBalance = accountBalance;
        if (accountBalance != null) {
            textDiamondAll.setText(accountBalance.getAmountDiamond() + "");
            textCoulduse.setText(getString(R.string.personal_account_diamond_deposible) + accountBalance.getAmount() + " 元");
            textStoneAll.setText(accountBalance.getAmountGemstone() + "");
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

    //////////////////////////////////////////

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    private void netGetAccountData() {
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(UserService.class).accountBalance(),
                new ErrorHandleSubscriber<BaseJson<AccountBalance>>() {
                    @Override
                    public void onNext(BaseJson<AccountBalance> basejson) {
                        AccountBalance accountBalance = basejson.getData();
                        setAccountData(accountBalance);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
