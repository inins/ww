package com.wang.social.moneytree.runalone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.widget.EditText;

import com.frame.base.BasicActivity;
import com.frame.component.helper.NetLoginTestHelper;
import com.frame.component.utils.SpannableStringUtil;
import com.frame.component.view.DialogPay;
import com.frame.di.component.AppComponent;
import com.frame.mvp.IView;
import com.wang.social.moneytree.R;
import com.wang.social.moneytree.R2;
import com.wang.social.moneytree.mvp.ui.GameListActivity;
import com.wang.social.moneytree.mvp.ui.widget.CountDownTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BasicActivity implements IView {
    @BindView(R2.id.edit_text)
    EditText mEditText;
    @BindView(R2.id.name_edit_text)
    EditText mNameET;
    @BindView(R2.id.password_edit_text)
    EditText mPasswordET;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.mt_activity_main;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
    }

    @OnClick(R2.id.confirm_button)
    public void confirm() {
        int groupId = 1;

        try {
            groupId = Integer.parseInt(mEditText.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        GameListActivity.start(this, groupId);
    }

    @OnClick(R2.id.login_button)
    public void login() {
        NetLoginTestHelper.newInstance().loginTest(mNameET.getText().toString(), mPasswordET.getText().toString());
    }

    @OnClick(R2.id.pay_button)
    public void pay() {
        String[] strings = {
                getString(R.string.mt_format_pay_title_pre),
                Integer.toString(100),
                getString(R.string.mt_format_pay_title_suf)};
        int[] colors = {
                ContextCompat.getColor(this, R.color.common_text_blank),
                ContextCompat.getColor(this, R.color.common_blue_deep),
                ContextCompat.getColor(this, R.color.common_text_blank)
        };
        SpannableStringBuilder titleText = SpannableStringUtil.createV2(strings, colors);
        DialogPay.showPay(this,
                getSupportFragmentManager(),
                titleText,
                getString(R.string.mt_format_pay_hint),
                getString(R.string.common_cancel),
                getString(R.string.mt_pay_immediately),
                getString(R.string.mt_recharge_immediately),
                100,
                9999,
                new DialogPay.DialogPayCallback() {

                    @Override
                    public void onPay() {

                    }
                });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
