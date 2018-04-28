package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.di.component.DaggerSingleActivityComponent;
import com.wang.social.personal.mvp.entities.AccountBalance;
import com.wang.social.personal.mvp.model.api.UserService;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountExchangeActivity extends BasicAppActivity implements IView {

    @BindView(R2.id.text_stone_all)
    TextView textStoneAll;
    @BindView(R2.id.text_diamond_all)
    TextView textDiamondAll;
    @BindView(R2.id.edit_stone)
    EditText editStone;


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
        if (i == R.id.btn_excharge) {
            int stoneCount = StrUtil.str2int(editStone.getText().toString());
            netExchange(stoneCount);
        }
    }

    private void setAccountData(AccountBalance accountBalance) {
        if (accountBalance != null) {
            textStoneAll.setText(accountBalance.getAmountGemstone() + "");
            textDiamondAll.setText(accountBalance.getAmountDiamond() + "");
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

    ////////////////////////////////

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    /////////////////////////////

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

    private void netExchange(int stoneCount) {
        Map<String, Object> param = NetParam.newInstance()
                .put("price", stoneCount)
                .put("objectType", "exchange")
                .put("payChannels", "aliPay")
                .put("versionCode", "2.0.0")
                .put("channelCode", "1")
                .put("v", "2.0.0")
                .putSignature()
                .build();
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(CommonService.class).exchangeStone(param),
                new ErrorHandleSubscriber<BaseJson<Object>>() {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        editStone.setText("");
                        netGetAccountData();
                        EventBus.getDefault().post(new EventBean(EventBean.EVENT_ACCOUNT_EXCHANGE_STONE));
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
