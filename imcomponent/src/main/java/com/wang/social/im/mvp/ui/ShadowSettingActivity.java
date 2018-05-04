package com.wang.social.im.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.RegexUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerShadowSettingComponent;
import com.wang.social.im.di.modules.ShadowSettingModule;
import com.wang.social.im.mvp.contract.ShadowSettingContract;
import com.wang.social.im.mvp.presenter.ShadowSettingPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 分身设置
 */
public class ShadowSettingActivity extends BaseAppActivity<ShadowSettingPresenter> implements ShadowSettingContract.View {

    @BindView(R2.id.ss_toolbar)
    SocialToolbar ssToolbar;
    @BindView(R2.id.ss_iv_portrait)
    ImageView ssIvPortrait;
    @BindView(R2.id.ss_et_nickname)
    EditText ssEtNickname;
    @BindView(R2.id.ss_rg_gender)
    RadioGroup ssRgGender;
    @BindView(R2.id.ss_tv_sure)
    TextView ssTvSure;
    @BindView(R2.id.ss_tv_shadow)
    TextView ssTvShadow;

    String socialId;

    private String mPortraitPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerShadowSettingComponent
                .builder()
                .appComponent(appComponent)
                .shadowSettingModule(new ShadowSettingModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_shadow_setting;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {

    }

    private void init() {
        ssEtNickname.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.length() + ssEtNickname.length() > 8) {
                    return "";
                }
                if (!RegexUtils.isUsernameMe(source)) {
                    return "";
                }
                return source;
            }
        }});

        ssToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
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

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick({R2.id.ss_iv_portrait, R2.id.ss_tv_sure})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.ss_iv_portrait) {

        } else if (view.getId() == R.id.ss_tv_sure) {

        }
    }
}