package com.wang.social.im.mvp.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.im.R;
import com.wang.social.im.mvp.contract.CreateTeamContract;
import com.wang.social.im.mvp.presenter.CreateTeamPrensenter;

/**
 * 创建觅聊
 */
public class CreateTeamActivity extends BaseAppActivity<CreateTeamPrensenter> implements CreateTeamContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_create_team;
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
