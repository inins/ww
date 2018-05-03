package com.wang.social.im.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.im.R;
import com.wang.social.im.di.component.DaggerShadowSettingComponent;
import com.wang.social.im.di.modules.ShadowSettingModule;
import com.wang.social.im.mvp.contract.ShadowSettingContract;
import com.wang.social.im.mvp.presenter.ShadowSettingPresenter;

/**
 * 分身设置
 */
public class ShadowSettingActivity extends BaseAppActivity<ShadowSettingPresenter> implements ShadowSettingContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}