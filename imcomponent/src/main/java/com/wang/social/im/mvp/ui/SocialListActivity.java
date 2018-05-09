package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerSocialListComponent;
import com.wang.social.im.di.modules.SocialListModule;
import com.wang.social.im.mvp.contract.SocialListContract;
import com.wang.social.im.mvp.model.entities.SocialListLevelOne;
import com.wang.social.im.mvp.presenter.SocialListPresenter;
import com.wang.social.im.mvp.ui.adapters.sociallist.SocialListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 趣聊列表
 */
public class SocialListActivity extends BaseAppActivity<SocialListPresenter> implements SocialListContract.View {

    @BindView(R2.id.sl_rlv_socials)
    RecyclerView slRlvSocials;

    public static void start(Context context){
        Intent intent = new Intent(context, SocialListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSocialListComponent
                .builder()
                .appComponent(appComponent)
                .socialListModule(new SocialListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_social_list;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mPresenter.getSocials();
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
    public void showSocials(List<SocialListLevelOne> socials) {
        slRlvSocials.setLayoutManager(new LinearLayoutManager(this));
        List list = new ArrayList(socials);
        slRlvSocials.setAdapter(new SocialListAdapter(list));
    }

    @OnClick({R2.id.sl_iv_search, R2.id.sl_iv_add})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.sl_iv_search) {

        } else if (view.getId() == R.id.sl_iv_add) {
            CreateSocialActivity.start(this);
        }
    }
}
