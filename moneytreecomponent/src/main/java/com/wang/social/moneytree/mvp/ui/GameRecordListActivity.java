package com.wang.social.moneytree.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.moneytree.R;
import com.wang.social.moneytree.R2;
import com.wang.social.moneytree.di.component.DaggerGameRecordListComponent;
import com.wang.social.moneytree.di.module.GameRecordListModule;
import com.wang.social.moneytree.mvp.contract.GameRecordListContract;
import com.wang.social.moneytree.mvp.model.entities.GameRecord;
import com.wang.social.moneytree.mvp.presenter.GameRecordListPresenter;
import com.wang.social.moneytree.mvp.ui.adapter.GameRecordListAdapter;
import com.wang.social.moneytree.mvp.ui.widget.PWRecordType;

import butterknife.BindView;
import butterknife.OnClick;

public class GameRecordListActivity extends BaseAppActivity<GameRecordListPresenter>
        implements GameRecordListContract.View ,PWRecordType.TypeListener,
        GameRecordListAdapter.ClickListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, GameRecordListActivity.class);
        context.startActivity(intent);
    }

    @BindView(R2.id.toolbar)
    SocialToolbar mToolbar;
    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    GameRecordListAdapter mAdapter;

    private PWRecordType mPWRecordType;
    private int mRecordType = 0;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGameRecordListComponent.builder()
                .appComponent(appComponent)
                .gameRecordListModule(new GameRecordListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.mt_activity_game_record_list;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    finish();
                }
            }
        });

        mAdapter = new GameRecordListAdapter(mRecyclerView, mPresenter.getRecordList());
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this, LinearLayoutManager.VERTICAL, false));

        // 更新，加载更多
        mSpringView.setHeader(new AliHeader(mSpringView.getContext(), false));
        mSpringView.setFooter(new AliFooter(mSpringView.getContext(), false));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadRecordList(true, mRecordType);
            }

            @Override
            public void onLoadmore() {
                mPresenter.loadRecordList(false, mRecordType);
            }
        });
        mSpringView.callFreshDelay();
    }

    @OnClick(R2.id.type_select_layout)
    public void selectType(View view) {
        if (null == mPWRecordType) {
            mPWRecordType = new PWRecordType(this);
            mPWRecordType.setTypeListener(this);
        }

        mPWRecordType.showPopupWindow(view);
    }

    @Override
    public void onTypeSelected(int type) {
        mRecordType = type;

        mSpringView.callFresh();
    }

    @Override
    public void onLoadRecordListSuccess() {
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadRecordListCompleted() {
        mSpringView.onFinishFreshAndLoadDelay();
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
    public void onEnterRecordDetail(GameRecord gameRecord) {
        GameRoomActivity.startGameRecord(this, gameRecord);
    }
}
