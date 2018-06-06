package com.wang.social.moneytree.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.LoadingLayoutEx;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.integration.AppManager;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.moneytree.R;
import com.wang.social.moneytree.R2;
import com.wang.social.moneytree.di.component.DaggerGameListComponent;
import com.wang.social.moneytree.di.module.GameListModule;
import com.wang.social.moneytree.mvp.contract.GameListContract;
import com.wang.social.moneytree.mvp.model.entities.GameBean;
import com.frame.component.entities.NewMoneyTreeGame;
import com.wang.social.moneytree.mvp.presenter.GameListPresenter;
import com.wang.social.moneytree.mvp.ui.adapter.GameListAdapter;
import com.frame.component.view.moneytree.DialogCreateGame;
import com.wang.social.moneytree.utils.Keys;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static com.wang.social.moneytree.utils.Keys.NAME_GAME_TYPE;
import static com.wang.social.moneytree.utils.Keys.NAME_GROUP_ID;
import static com.wang.social.moneytree.utils.Keys.TYPE_FROM_GROUP;
import static com.wang.social.moneytree.utils.Keys.TYPE_FROM_SQUARE;

@RouteNode(path = "/money_tree_list", desc = "游戏列表")
public class GameListActivity extends BaseAppActivity<GameListPresenter>
        implements GameListContract.View, DialogCreateGame.CreateGameCallback,
        GameListAdapter.ClickListener {


    public static void startFromGroup(Context context, int groupId) {
        Intent intent = new Intent(context, GameListActivity.class);
        intent.putExtra(NAME_GAME_TYPE, TYPE_FROM_GROUP);
        intent.putExtra(NAME_GROUP_ID, groupId);
        context.startActivity(intent);
    }

    public static void startFromSquare(Context context) {
        Intent intent = new Intent(context, GameListActivity.class);
        intent.putExtra(NAME_GAME_TYPE, TYPE_FROM_SQUARE);
        context.startActivity(intent);
    }

    @BindView(R2.id.toolbar)
    SocialToolbar mToolbar;
    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    GameListAdapter mAdapter;

    @BindView(R2.id.no_content_layout)
    View mNoContentLayout;

    // 群ID (在群中创建时必传)
    private int mGroupId;
    // 创建类型（1：通过群，2：活动与游戏）
    private @Keys.GameType
    int mType;

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
//        StatusBarUtil.setTranslucent(this);
//        StatusBarUtil.setTextDark(this);

        mType = getIntent().getIntExtra(NAME_GAME_TYPE, TYPE_FROM_SQUARE);
        mGroupId = getIntent().getIntExtra(NAME_GROUP_ID, -1);

        mToolbar.setOnButtonClickListener(clickType -> {
            if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                finish();
            } else if (clickType == SocialToolbar.ClickType.RIGHT_TEXT) {
                GameRecordListActivity.start(GameListActivity.this);
            }
        });

        mAdapter = new GameListAdapter(this, mPresenter.getGameList());
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
    public void showToastShort(String msg) {
        ToastUtil.showToastShort(msg);
    }

    @Override
    public void onLoadGameListSuccess() {
        refreshGameList();
    }

    private void refreshGameList() {
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadGameListCompleted() {
        mSpringView.onFinishFreshAndLoadDelay();

        if (mPresenter.getGameList().size() <= 0) {
            mNoContentLayout.setVisibility(View.VISIBLE);
        } else {
            mNoContentLayout.setVisibility(View.GONE);
        }
    }


    @OnClick(R2.id.create_game_image_view)
    public void createGame() {
        if (mType == TYPE_FROM_GROUP) {
            DialogCreateGame.createFromGroup(this, getSupportFragmentManager(), mGroupId, this);
        } else if (mType == TYPE_FROM_SQUARE) {
            DialogCreateGame.createFromSquare(this, getSupportFragmentManager(), this);
        }
    }

    @Override
    public void onEnterGameRoom(GameBean gameBean) {
        GameRoomActivity.startGame(this, gameBean, mType);
    }

    @Override
    public boolean onCreateSuccess(NewMoneyTreeGame newMoneyTreeGame) {
        return true;
    }

    @Override
    public void onPayCreateGameSuccess(int roomId, int diamond) {
        // 刷新页面
        mSpringView.callFreshDelay();
        // 进入游戏房间
        GameBean gameBean = new GameBean();
        gameBean.setRoomId(roomId);
        GameRoomActivity.startGame(this, gameBean, mType);
    }


    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        super.onCommonEvent(event);

        switch (event.getEvent()) {
            case EventBean.EVENT_GAME_RESULT:
                Timber.i("游戏结束，刷新列表");
                // 游戏结束,刷新页面
                mSpringView.callFreshDelay();
                break;
            case EventBean.EVENT_GAME_JOIN:
                Timber.i("有人加入游戏，刷新列表");
                // 有人加入游戏，刷新列表
                mSpringView.callFreshDelay();
                break;
        }
    }

}
