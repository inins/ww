package com.wang.social.im.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.TitleView;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.contract.FunPointContract;
import com.wang.social.im.mvp.presenter.FunPointPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * 觅聊趣点列表
 */
public class TeamFunPointActivity extends BaseAppActivity<FunPointPresenter> implements FunPointContract.View {

    @BindView(R2.id.tfp_title)
    TitleView tfpTitle;
    @BindView(R2.id.tfp_rlv_news)
    RecyclerView tfpRlvNews;
    @BindView(R2.id.tfp_loader)
    SpringView tfpLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_team_fun_point;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        tfpLoader.setHeader(new AliHeader(this, false));
        tfpLoader.setFooter(new AliFooter(this, false));

        tfpRlvNews.setLayoutManager(new LinearLayoutManager(this));
        tfpRlvNews.addItemDecoration(new GridSpacingItemDecoration(1, SizeUtils.dp2px(5), GridLayoutManager.HORIZONTAL, false));

        tfpLoader.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadmore() {

            }
        });
    }

    @Override
    public void showFunPoints(List<Funpoint> data, boolean hasMore) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}