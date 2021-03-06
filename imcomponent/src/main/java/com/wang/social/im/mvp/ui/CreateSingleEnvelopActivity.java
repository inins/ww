package com.wang.social.im.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.common.InputLengthFilter;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.RegexConstants;
import com.frame.utils.RegexUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.di.component.DaggerCreateEnvelopComponent;
import com.wang.social.im.di.modules.CreateEnvelopModule;
import com.wang.social.im.mvp.contract.CreateEnvelopContract;
import com.wang.social.im.mvp.model.entities.EnvelopInfo;
import com.wang.social.im.mvp.presenter.CreateEnvelopPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发送个人红包
 */
public class CreateSingleEnvelopActivity extends BaseAppActivity<CreateEnvelopPresenter> implements CreateEnvelopContract.View {

    @BindView(R2.id.toolbar)
    SocialToolbar crpToolbar;
    @BindView(R2.id.crp_et_diamond)
    EditText crpEtDiamond;
    @BindView(R2.id.crp_et_message)
    EditText crpEtMessage;
    @BindView(R2.id.crp_tv_diamond)
    TextView crpTvDiamond;
    @BindView(R2.id.crp_tvb_plug)
    TextView crpTvbPlug;

    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, CreateSingleEnvelopActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCreateEnvelopComponent.builder()
                .appComponent(appComponent)
                .createEnvelopModule(new CreateEnvelopModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_create_single_envelop;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        init();
    }

    private void init() {
        crpEtMessage.setFilters(new InputFilter[]{new InputLengthFilter(20, false)});
        crpEtDiamond.setFilters(new InputFilter[]{new MoneyInputFilter()});

        crpTvDiamond.setText(UIUtil.getString(R.string.common_diamond_format_space, 0));

        crpEtDiamond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && Integer.valueOf(s.toString()) > 0) {
                    crpTvbPlug.setEnabled(true);
                    crpTvDiamond.setText(UIUtil.getString(R.string.common_diamond_format_space, Integer.valueOf(s.toString())));
                } else {
                    crpTvbPlug.setEnabled(false);
                    crpTvDiamond.setText(UIUtil.getString(R.string.common_diamond_format_space, 0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        crpToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                }
            }
        });
    }

    @OnClick(R2.id.crp_tvb_plug)
    public void onViewClicked() {
        int diamond = Integer.parseInt(crpEtDiamond.getText().toString());
        if (diamond > IMConstants.PRIVATE_ENVELOP_DIAMOND_LIMIT) {
            ToastUtil.showToastShort(UIUtil.getString(R.string.im_envelop_toast_limit, IMConstants.PRIVATE_ENVELOP_DIAMOND_LIMIT));
            return;
        }
        String message = crpEtMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            message = getString(R.string.im_envelop_message_default);
        }
        mPresenter.createEnvelop(EnvelopInfo.EnvelopType.PRIVATE, diamond, 1, message, "");
    }

    @Override
    public void showLoadingDialog() {
        super.showLoadingDialog();
        dialogLoading.get().setCancelable(false);
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
    public void onCreateSuccess(long envelopId, String message) {
        Intent intent = new Intent();
        intent.putExtra("envelopId", envelopId);
        intent.putExtra("message", message);
        setResult(RESULT_OK, intent);
        finish();
    }

    class MoneyInputFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            if (i1 + i3 > 9) {
                return "";
            }
            if (spanned.length() == 0 && charSequence.equals("0")) {
                return "";
            }
            if (RegexUtils.isMatch(RegexConstants.REGEX_NOT_NEGATIVE_INTEGER, charSequence)) {
                return charSequence;
            }
            return "";
        }
    }
}
