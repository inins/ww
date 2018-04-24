package com.wang.social.im.mvp.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.common.InputPositiveIntegerFilter;
import com.wang.social.im.mvp.contract.CreateEnvelopContract;
import com.wang.social.im.mvp.presenter.CreateEnvelopPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateMultiEnvelopActivity extends BaseAppActivity<CreateEnvelopPresenter> implements CreateEnvelopContract.View {

    @BindView(R2.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R2.id.cmr_et_diamond)
    EditText cmrEtDiamond;
    @BindView(R2.id.cmr_tv_now_mode)
    TextView cmrTvNowMode;
    @BindView(R2.id.cmr_tv_diamond)
    TextView cmrTvDiamond;
    @BindView(R2.id.cmr_tv_click_change)
    TextView cmrTvClickChange;
    @BindView(R2.id.cmr_et_count)
    EditText cmrEtCount;
    @BindView(R2.id.cmr_tv_group_member)
    TextView cmrTvGroupMember;
    @BindView(R2.id.cmr_et_message)
    EditText cmrEtMessage;
    @BindView(R2.id.cmr_tv_post_diamond)
    TextView cmrTvPostDiamond;
    @BindView(R2.id.cmr_tvb_plug)
    TextView cmrTvbPlug;

    //标识当前是否为拼手气模式
    private boolean isSpell = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_create_multi_envelop;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        init();
    }

    private void init() {
        cmrEtDiamond.setFilters(new InputFilter[]{new InputPositiveIntegerFilter()});
        cmrEtCount.setFilters(new InputFilter[]{new InputPositiveIntegerFilter()});

        toggleMode();

        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType){
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                }
            }
        });

        cmrEtDiamond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && cmrEtCount.length() > 0) {
                    cmrTvbPlug.setEnabled(true);
                } else {
                    cmrTvbPlug.setEnabled(false);
                }
                showPayDiamond();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cmrEtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && cmrEtDiamond.length() > 0) {
                    cmrTvbPlug.setEnabled(true);
                } else {
                    cmrTvbPlug.setEnabled(false);
                }
                showPayDiamond();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick({R2.id.cmr_tv_click_change, R2.id.cmr_tvb_plug})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.cmr_tv_click_change){
            isSpell = !isSpell;
            toggleMode();
        }else if (view.getId() == R.id.cmr_tvb_plug){

        }
    }

    private void toggleMode() {
        if (isSpell) {
            cmrTvDiamond.setText(R.string.im_envelop_total_money);
            cmrTvNowMode.setText(UIUtil.getString(R.string.im_envelop_now_mode, getString(R.string.im_envelop_spell)));
            cmrTvClickChange.setText(UIUtil.getString(R.string.im_envelop_click_change, getString(R.string.im_envelop_equal)));

            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.im_ic_spell);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            cmrTvDiamond.setCompoundDrawables(drawable, null, null, null);
            cmrTvDiamond.setCompoundDrawablePadding(SizeUtils.dp2px(5));
        } else {
            cmrTvDiamond.setText(R.string.im_envelop_single_money);
            cmrTvNowMode.setText(UIUtil.getString(R.string.im_envelop_now_mode, getString(R.string.im_envelop_equal)));
            cmrTvClickChange.setText(UIUtil.getString(R.string.im_envelop_click_change, getString(R.string.im_envelop_spell)));

            cmrTvDiamond.setCompoundDrawables(null, null, null, null);
        }

        showPayDiamond();
    }

    private void showPayDiamond() {
        int diamondSize = getPayDiamond();
        cmrTvPostDiamond.setText(UIUtil.getString(R.string.common_diamond_format_space, diamondSize));
    }

    private int getPayDiamond() {
        String diamondStr = cmrEtDiamond.getText().toString();
        if (isSpell) {
            return TextUtils.isEmpty(diamondStr) ? 0 : Integer.parseInt(diamondStr);
        } else {
            String countStr = cmrEtCount.getText().toString();
            int diamond = TextUtils.isEmpty(diamondStr) ? 0 : Integer.parseInt(diamondStr);
            int count = TextUtils.isEmpty(countStr) ? 0 : Integer.parseInt(countStr);
            return diamond * count;
        }
    }
}
