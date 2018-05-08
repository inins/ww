package com.wang.social.moneytree.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BasicActivity;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.moneytree.R;
import com.wang.social.moneytree.R2;
import com.wang.social.moneytree.di.component.DaggerGameListComponent;
import com.wang.social.moneytree.di.module.GameListModule;
import com.wang.social.moneytree.mvp.contract.GameListContract;
import com.wang.social.moneytree.mvp.model.entities.NewGame;
import com.wang.social.moneytree.mvp.presenter.GameListPresenter;
import com.wang.social.moneytree.mvp.ui.adapter.GameListAdapter;
import com.wang.social.moneytree.mvp.ui.widget.DialogCreateGame;

import butterknife.BindView;
import butterknife.OnClick;

public class GameListActivity extends BaseAppActivity<GameListPresenter>
        implements GameListContract.View, DialogCreateGame.CreateGameCallback {

    @BindView(R2.id.toolbar)
    SocialToolbar mToolbar;
    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    GameListAdapter mAdapter;

    private DialogCreateGame mDialogCreateGame;
    // 群ID (在群中创建时必传)
    private int mGroupId;
    // 创建类型（1：通过群，2：用户）
    private int mType;
    // 游戏类型（1：人数，2：时间）
    private int mGameType;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGameListComponent.builder()
                .appComponent(appComponent)
                .gameListModule(new GameListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.mt_activity_game_list;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mGroupId = getIntent().getIntExtra("groupId", -1);
        mType = getIntent().getIntExtra("type", 1);
        mGameType = getIntent().getIntExtra("gameType", 1);

        mToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    finish();
                } else if (clickType == SocialToolbar.ClickType.RIGHT_TEXT) {
                    GameRecordListActivity.start(GameListActivity.this);
                }
            }
        });

        mAdapter = new GameListAdapter(mRecyclerView, mPresenter.getGameList());
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
                mPresenter.getMoneyTreeList(true);
            }

            @Override
            public void onLoadmore() {
                mPresenter.getMoneyTreeList(false);
            }
        });
        mSpringView.callFreshDelay();
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
    public void showToastLong(String msg) {
        ToastUtil.showToastLong(msg);
    }

    @Override
    public void onLoadGameListSuccess() {
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadGameListCompleted() {
        mSpringView.onFinishFreshAndLoadDelay();
    }

    @Override
    public void onCreateGameSuccess(NewGame newGame) {
        if (null != mDialogCreateGame) {
            mDialogCreateGame.dismiss();
            mDialogCreateGame = null;
        }
    }

    @Override
    public void onCreateGameCompleted() {

    }

    @OnClick(R2.id.create_game_image_view)
    public void createGame() {
        mDialogCreateGame = DialogCreateGame.show(getSupportFragmentManager(), this);
    }

    /**
     * 创建游戏
     *
     *  groupId   群ID (在群中创建时必传)
     *  type      创建类型（1：通过群，2：用户）
     *  gameType  游戏类型（1：人数，2：时间）
     * @param resetTime 重置时长(s)
     * @param diamond   钻石数
     * @param peopleNum 开始人数 (gameType=1时必传)
     */
    @Override
    public void createGame(int resetTime, int peopleNum, int diamond) {
        mPresenter.createGame(mGroupId, mType, mGameType, resetTime, peopleNum, diamond);
    }
}
