package com.wang.social.im.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.RegexConstants;
import com.frame.utils.RegexUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.contract.CreateEnvelopContract;
import com.wang.social.im.mvp.presenter.CreateEnvelopPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发送个人红包
 */
public class CreateSingleEnvelopActivity extends BaseAppActivity<CreateEnvelopPresenter> implements CreateEnvelopContract.View{

    @BindView(R2.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R2.id.crp_et_diamond)
    EditText crpEtDiamond;
    @BindView(R2.id.crp_et_message)
    EditText crpEtMessage;
    @BindView(R2.id.crp_tv_diamond)
    TextView crpTvDiamond;
    @BindView(R2.id.crp_tvb_plug)
    TextView crpTvbPlug;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

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
    }

    @OnClick(R2.id.crp_tvb_plug)
    public void onViewClicked() {
        int diamond = Integer.parseInt(crpEtDiamond.getText().toString());
        if (diamond > 20000) {
            ToastUtil.showToastShort("红包钻石数不能大于20000");
            return;
        }

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
