package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.helper.AppValiHelper;
import com.wang.social.personal.mvp.entities.AccountBalance;
import com.wang.social.personal.mvp.model.api.UserService;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Field;

public class AccountDepositActivity extends BasicAppNoDiActivity {

    @BindView(R2.id.text_diamond)
    TextView textDiamond;
    @BindView(R2.id.text_money)
    TextView textMoney;
    @BindView(R2.id.edit_money)
    EditText editMoney;
    @BindView(R2.id.edit_aliaccount)
    EditText editAliaccount;
    @BindView(R2.id.edit_realname)
    EditText editRealname;
    @BindView(R2.id.btn_go)
    TextView btnGo;
    private int diamond;
    private float amount;

    public static void start(Context context, int diamond, float amount) {
        Intent intent = new Intent(context, AccountDepositActivity.class);
        intent.putExtra("diamond", diamond);
        intent.putExtra("amount", amount);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_account_deposit;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        diamond = getIntent().getIntExtra("diamond", 0);
        amount = getIntent().getFloatExtra("amount", 0);
        setData();
    }

    private void setData() {
        textDiamond.setText(String.valueOf(diamond));
        textMoney.setText(String.valueOf(amount));
        btnGo.setEnabled(diamond >= 100);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
            AccountDepositRecordActivity.start(this);
        } else if (i == R.id.btn_go) {
            int money = StrUtil.str2int(editMoney.getText().toString());
            String account = editAliaccount.getText().toString();
            String name = editRealname.getText().toString();

            String msg = AppValiHelper.deposit(money, account, name);
            if (msg != null) {
                ToastUtil.showToastShort(msg);
            } else {
                netDeposit(money, account, name);
            }
        }
    }

    ////////////////////////////////////////

    private void netDeposit(int money, String account, String realName) {
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(UserService.class).deposit(money, account, realName),
                new ErrorHandleSubscriber<BaseJson<Object>>() {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        ToastUtil.showToastShort("提交成功");
                        finish();
                        AccountDepositRecordActivity.start(AccountDepositActivity.this);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
