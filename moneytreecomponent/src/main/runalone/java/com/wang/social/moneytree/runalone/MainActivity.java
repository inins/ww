package com.wang.social.moneytree.runalone;

import android.content.Intent;
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
import com.umeng.socialize.UMShareAPI;
import com.wang.social.moneytree.R;
import com.wang.social.moneytree.R2;
import com.wang.social.moneytree.mvp.ui.GameListActivity;
import com.wang.social.pictureselector.ActivityPicturePreview;
import com.wang.social.socialize.SocializeUtil;

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

    @OnClick(R2.id.pics_button)
    public void pics() {
        String[] pics = {
                "https://a.cdnsbn.com/images/products/l/20857459814.jpg",
                "https://a.cdnsbn.com/images/products/l/10005703602.jpg",
                "https://a.cdnsbn.com/images/products/l/12834780402.jpg",
                "https://b.cdnsbn.com/images/products/l/15403480402.jpg",
                "https://b.cdnsbn.com/images/products/l/05766096301.jpg",
                "https://c.cdnsbn.com/images/products/l/04010986801.jpg",
                "https://c.cdnsbn.com/images/products/l/16588798103.jpg",
                "https://c.cdnsbn.com/images/products/l/11440582501.jpg",
                "https://d.cdnsbn.com/images/products/l/07983430803.jpg"
        };
        ActivityPicturePreview.startBrowse(this, pics.length / 2, pics);
    }

    @OnClick(R2.id.confirm_button)
    public void confirm() {
        int groupId = 0;

        try {
            groupId = Integer.parseInt(mEditText.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (groupId > 0) {
            GameListActivity.startFromGroup(this, groupId);
        } else {
            GameListActivity.startFromSquare(this);
        }
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

    @OnClick(R2.id.share_button)
    public void share() {
        SocializeUtil.shareWeb(getSupportFragmentManager(),
                null,
                "http://www.wangsocial.com/",
                "往往",
                "有点2的社交软件",
                "http://resouce.dongdongwedding.com/activity_cashcow_moneyTree.png");
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
