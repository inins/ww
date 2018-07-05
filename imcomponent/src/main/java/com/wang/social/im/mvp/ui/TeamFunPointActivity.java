package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BaseAdapter;
import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.component.ui.adapter.RecycleAdapterFunpoint;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.TitleView;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.Autowired;
import com.frame.utils.SizeUtils;
import com.liaoinstan.springview.aliheader.AliFooter;
import com.liaoinstan.springview.aliheader.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerNobodyComponent;
import com.wang.social.im.di.component.DaggerTeamFunPointComponent;
import com.wang.social.im.di.modules.TeamFunPointModule;
import com.wang.social.im.mvp.contract.FunPointContract;
import com.wang.social.im.mvp.presenter.FunPointPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * 觅聊趣点列表
 */
public class TeamFunPointActivity extends BaseAppActivity<FunPointPresenter> implements FunPointContract.View, BaseAdapter.OnItemClickListener<Funpoint> {

    @BindView(R2.id.tfp_title)
    TitleView tfpTitle;
    @BindView(R2.id.tfp_rlv_news)
    RecyclerView tfpRlvNews;
    @BindView(R2.id.tfp_loader)
    SpringView tfpLoader;

    @Autowired
    String teamId;
    @Autowired
    String tag;

    private RecycleAdapterFunpoint mAdapter;

    public static void start(Context context, String teamId, String tagName) {
        Intent intent = new Intent(context, TeamFunPointActivity.class);
        intent.putExtra("teamId", teamId);
        intent.putExtra("tag", tagName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTeamFunPointComponent
                .builder()
                .appComponent(appComponent)
                .teamFunPointModule(new TeamFunPointModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_team_fun_point;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        initView();

        tfpTitle.setTitle(tag);

        tfpLoader.callFreshDelay();
    }

    private void initView() {
        tfpLoader.setHeader(new AliHeader(this, false));
        tfpLoader.setFooter(new AliFooter(this, false));

        tfpRlvNews.setLayoutManager(new LinearLayoutManager(this));
        tfpRlvNews.addItemDecoration(new GridSpacingItemDecoration(1, SizeUtils.dp2px(5), GridLayoutManager.HORIZONTAL, false));
        mAdapter = new RecycleAdapterFunpoint();
        mAdapter.setOnItemClickListener(this);
        tfpRlvNews.setAdapter(mAdapter);

        tfpLoader.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getFunPoints(teamId, true);
            }

            @Override
            public void onLoadmore() {
                mPresenter.getFunPoints(teamId, false);
            }
        });
    }

    @Override
    public void showFunPoints(List<Funpoint> data, boolean isRefresh, boolean hasMore) {
        if (isRefresh) {
            mAdapter.refreshData(data);
        } else {
            mAdapter.addItem(data);
        }
        if (hasMore) {
            tfpLoader.setEnableFooter(true);
        } else {
            tfpLoader.setEnableFooter(false);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        tfpLoader.onFinishFreshAndLoadDelay();
    }

    @Override
    public void onItemClick(Funpoint funpoint, int position) {

    }
}