package com.wang.social.moneytree.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.utils.SpannableStringUtil;
import com.frame.component.view.DialogPay;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.StatusBarUtil;
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
import com.wang.social.moneytree.mvp.model.entities.NewGame;
import com.wang.social.moneytree.mvp.presenter.GameListPresenter;
import com.wang.social.moneytree.mvp.ui.adapter.GameListAdapter;
import com.wang.social.moneytree.mvp.ui.widget.DialogCreateGame;

import butterknife.BindView;
import butterknife.OnClick;

public class GameListActivity extends BaseAppActivity<GameListPresenter>
        implements GameListContract.View, DialogCreateGame.CreateGameCallback,
        GameListAdapter.ClickListener {
    public final static String NAME_GROUP_ID = "NAME_GROUP_ID";

    public static void start(Context context, int groupId) {
        Intent intent = new Intent(context, GameListActivity.class);
        intent.putExtra(NAME_GROUP_ID, groupId);
        context.startActivity(intent);
    }

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
        StatusBarUtil.setTranslucent(this);
        StatusBarUtil.setTextDark(this);

        mGroupId = getIntent().getIntExtra(NAME_GROUP_ID, -1);
        mType = mGroupId == -1 ? 2 : 1;

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
    }

    @Override
    public void onCreateGameSuccess(NewGame newGame) {
        // 刷新页面
        mSpringView.callFreshDelay();

        // 隐藏创建游戏对话框
        if (null != mDialogCreateGame) {
            mDialogCreateGame.dismiss();
            mDialogCreateGame = null;
        }


        // 支付
        popupCreateGamePayDialog(newGame);
    }

    public void popupCreateGamePayDialog(NewGame newGame) {
        String[] strings = {
                getString(R.string.mt_format_pay_title_pre),
                Integer.toString(newGame.getDiamond()),
                getString(R.string.mt_format_pay_title_suf)};
        int[] colors = {
                ContextCompat.getColor(this, R.color.common_text_blank),
                ContextCompat.getColor(this, R.color.common_blue_deep),
                ContextCompat.getColor(this, R.color.common_text_blank)
        };
        SpannableStringBuilder titleText = SpannableStringUtil.createV2(strings, colors);
        DialogPay.showPay(this,
                getSupportFragmentManager(),
                titleText,
                getString(R.string.mt_format_pay_hint),
                getString(R.string.common_cancel),
                getString(R.string.mt_pay_immediately),
                getString(R.string.mt_recharge_immediately),
                newGame.getDiamond(),
                newGame.getBalance(),
                new DialogPay.DialogPayCallback() {

                    @Override
                    public void onPay() {
                        mPresenter.payCreateGame(newGame);
                    }
                });
    }

    @Override
    public void onCreateGameCompleted() {

    }

    @Override
    public void onPayCreateGameSuccess() {
        ToastUtil.showToastLong("创建游戏成功");

        // 刷新页面
        mSpringView.callFreshDelay();
    }

    @Override
    public void onPayCreateGameCompleted() {

    }

    @OnClick(R2.id.create_game_image_view)
    public void createGame() {
        mDialogCreateGame = DialogCreateGame.show(this, getSupportFragmentManager(), this);
    }

    /**
     * 创建游戏
     * <p>
     * groupId   群ID (在群中创建时必传)
     * type      创建类型（1：通过群，2：用户）
     * gameType  游戏类型（1：人数，2：时间）
     *
     * @param resetTime 重置时长(s)
     * @param diamond   钻石数
     * @param peopleNum 开始人数 (gameType=1时必传)
     */
    @Override
    public void createGame(int resetTime, int peopleNum, boolean unlimitedPeople, int diamond) {
        mPresenter.createGame(mGroupId, mType, unlimitedPeople ? 2 : 1, resetTime, diamond, peopleNum);
    }

    @Override
    public void onEnterGameRoom(GameBean gameBean) {
        GameRoomActivity.startGame(this, gameBean);
    }
}
