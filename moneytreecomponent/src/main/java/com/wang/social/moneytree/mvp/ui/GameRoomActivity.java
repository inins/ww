package com.wang.social.moneytree.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.utils.SpannableStringUtil;
import com.frame.component.view.DialogPay;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.integration.AppManager;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.moneytree.R;
import com.wang.social.moneytree.R2;
import com.wang.social.moneytree.di.component.DaggerGameRoomComponent;
import com.wang.social.moneytree.di.module.GameRoomModule;
import com.wang.social.moneytree.mvp.contract.GameRoomContract;
import com.wang.social.moneytree.mvp.model.entities.GameBean;
import com.wang.social.moneytree.mvp.model.entities.GameEnd;
import com.wang.social.moneytree.mvp.model.entities.GameRecord;
import com.wang.social.moneytree.mvp.model.entities.GameRecordDetail;
import com.wang.social.moneytree.mvp.model.entities.JoinGame;
import com.wang.social.moneytree.mvp.model.entities.RoomMsg;
import com.wang.social.moneytree.mvp.presenter.GameRoomPresenter;
import com.wang.social.moneytree.mvp.ui.adapter.GameRoomMemberListAdapter;
import com.wang.social.moneytree.mvp.ui.adapter.GameScoreAdapter;
import com.wang.social.moneytree.mvp.ui.widget.CountDownTextView;
import com.wang.social.moneytree.mvp.ui.widget.DialogGameOver;
import com.wang.social.moneytree.utils.ShakeUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class GameRoomActivity extends BaseAppActivity<GameRoomPresenter>
        implements GameRoomContract.View, CountDownTextView.CountDownListener,
        ShakeUtils.OnShakeListener, DialogGameOver.GameOverListener{
    public final static String NAME_GAME_BEAN = "NAME_GAME_BEAN";
    public final static String NAME_GAME_RECORD = "NAME_GAME_RECORD";

    public static void startGame(Context context, GameBean gameBean) {
        Intent intent = new Intent(context, GameRoomActivity.class);
        intent.putExtra(NAME_GAME_BEAN, gameBean);
        context.startActivity(intent);
    }

    public static void startGameRecord(Context context, GameRecord gameRecord) {
        Intent intent = new Intent(context, GameRoomActivity.class);
        intent.putExtra(NAME_GAME_RECORD, gameRecord);
        context.startActivity(intent);
    }

    @BindView(R2.id.toolbar)
    SocialToolbar mToolbar;
    // 标题
    @BindView(R2.id.title_text_view)
    TextView mTitleTV;
    @BindView(R2.id.content_layout)
    View mContentLayout;
    // 倒计时
    @BindView(R2.id.count_down_layout)
    View mCountDownLayout;
    @BindView(R2.id.count_down_text_view)
    CountDownTextView mCountDownTV;
    // 参与人数
    @BindView(R2.id.join_num_layout)
    View mJoinNumLayout;
    @BindView(R2.id.join_num_text_view)
    TextView mJoinNumTV;
    // 累计xx钻
    @BindView(R2.id.diamon_num_text_view)
    TextView mDiamondNumTV;
    // 立即参与
    @BindView(R2.id.join_immediately_image_view)
    ImageView mJoinGameIV;
    // 游戏成员列表
    @BindView(R2.id.member_list_recycler_view)
    RecyclerView mMemberListRV;
    GameRoomMemberListAdapter mMemberAdapter;
    GameScoreAdapter mGameScoreAdapter;
    // 进入趣聊
    @BindView(R2.id.go_chat_image_view)
    ImageView mGoChatIV;
    // 游戏规则
    @BindView(R2.id.game_rule_image_view)
    ImageView mGameRuleIV;
    // 游戏列表
    @BindView(R2.id.game_list_image_view)
    ImageView mGameListIV;
    // 摇一摇提示
    @BindView(R2.id.shake_text_view)
    TextView mShakeHintTV;

    private ShakeUtils mShakeUtils;
    @Inject
    AppManager mAppManager;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGameRoomComponent.builder()
                .appComponent(appComponent)
                .gameRoomModule(new GameRoomModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.mt_activity_game_room;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        StatusBarUtil.setTranslucent(this);
//        StatusBarUtil.setTextDark(this);

        mShakeUtils = new ShakeUtils(this);
        mShakeUtils.setOnShakeListener(this);

        // 游戏信息
        mPresenter.setGameBean(getIntent().getParcelableExtra(NAME_GAME_BEAN));
        // 游戏记录信息
        mPresenter.setGameRecord(getIntent().getParcelableExtra(NAME_GAME_RECORD));

        mToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    finish();                }
            }
        });

        // 游戏房间
        if (null != mPresenter.getGameBean()) {
            // 成员列表
            mMemberAdapter = new GameRoomMemberListAdapter(mMemberListRV, mPresenter.getMemberList());
            mMemberListRV.setLayoutManager(
                    new LinearLayoutManager(
                            this,
                            LinearLayoutManager.HORIZONTAL,
                            false));
            mMemberListRV.setAdapter(mMemberAdapter);

            mPresenter.getRoomMsg(mPresenter.getRoomId());
        } else if (null != mPresenter.getGameRecord()) {
            mGameScoreAdapter = new GameScoreAdapter(mMemberListRV, mPresenter.getGameScoreList());
            mMemberListRV.setLayoutManager(
                    new LinearLayoutManager(
                            this,
                            LinearLayoutManager.VERTICAL,
                            false));
            mMemberListRV.setAdapter(mGameScoreAdapter);
            // 游戏记录
            mPresenter.loadRecordDetail(mPresenter.getGameRecordGameId());
        }
    }

    @Override
    public void showToastLong(String msg) {
        ToastUtil.showToastLong(msg);
    }

    @Override
    public void onGetRecordDetailSuccess(GameRecordDetail recordDetail) {
        // 隐藏
        mCountDownLayout.setVisibility(View.GONE);
        mJoinNumLayout.setVisibility(View.GONE);
        mGoChatIV.setVisibility(View.INVISIBLE);
        mGameRuleIV.setVisibility(View.INVISIBLE);
        mJoinGameIV.setVisibility(View.GONE);
        mShakeHintTV.setVisibility(View.GONE);

        // 房间名
        mTitleTV.setText(recordDetail.getRoomName());

        if (null != mGameScoreAdapter) {
            mGameScoreAdapter.notifyDataSetChanged();
        }

        // 显示
        mContentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadGameEndSuccess(GameEnd gameEnd) {
        // 显示对话框
        DialogGameOver.show(getSupportFragmentManager(), gameEnd, this);
    }

    @Override
    public void onGetRoomMsgSuccess(RoomMsg roomMsg) {
        // 房间名
        mTitleTV.setText(roomMsg.getRoomName());
        // 倒计时
        mCountDownTV.start(roomMsg.getResetTime());
        mCountDownTV.setCountDownListener(this);
        // xx人参与中
        setJoinNumTV(roomMsg.getJoinNum());
        // 累计xx钻
        mDiamondNumTV.setText(String.format(getString(R.string.mt_format_game_room_diamond_num), roomMsg.getDiamond()));
        // 是否已加入游戏
        // :isJoin:是否加入该游戏(0：未加入，1:加入);
        if (roomMsg.getIsJoin() == 0) {
            // 未加入游戏
            mJoinGameIV.setVisibility(View.VISIBLE);
        } else {
            // 已加入游戏
            mJoinGameIV.setVisibility(View.GONE);

            // 加载成员列表
            mPresenter.loadMemberList();
        }

        // 显示
        mContentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void popupJoinGamePayDialog(JoinGame joinGame) {
        String[] strings = {
                getString(R.string.mt_format_pay_title_pre),
                Integer.toString(joinGame.getDiamond()),
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
                joinGame.getDiamond(),
                joinGame.getBalance(),
                new DialogPay.DialogPayCallback() {

                    @Override
                    public void onPay() {
                        mPresenter.payJoinGame(joinGame);
                    }
                });
    }

    @Override
    public void onPayJoinGameSuccess() {
        // 加入游戏支付成功
        ToastUtil.showToastLong("参与成功");
        // 刷新游戏
        mPresenter.getRoomMsg(mPresenter.getRoomId());
    }

    @Override
    public void onLoadMemberListSuccess() {
        // 刷新成员列表
        if (null != mMemberAdapter) {
            mMemberAdapter.notifyDataSetChanged();
        }
    }

    private void setJoinNumTV(int num) {
        String[] strings = {
                Integer.toString(num) + getString(R.string.mt_people),
                getString(R.string.mt_join_in)};
        int[] colors = {
                Color.parseColor("#FFF111"),
                getResources().getColor(R.color.common_white)};
        SpannableStringBuilder text = SpannableStringUtil.createV2(strings, colors);
        mJoinNumTV.setText(text);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    /**
     * 立即参与
     */
    @OnClick(R2.id.join_immediately_image_view)
    public void joinImmediately() {
        mPresenter.joinGame(mPresenter.getRoomId());
    }

    /**
     * 去聊
     */
    @OnClick(R2.id.go_chat_image_view)
    public void goToChat() {

    }

    /**
     * 游戏规则
     */
    @OnClick(R2.id.game_rule_image_view)
    public void goToGameRule() {

    }

    /**
     * 游戏列表
     */
    @OnClick(R2.id.game_list_image_view)
    public void goToGameList() {
        mAppManager.killActivity(GameRecordListActivity.class);
        finish();
    }

    @Override
    public void onCountDownFinished() {
//        ToastUtil.showToastLong("游戏结束");
        // 游戏结束，加载结果
        mPresenter.loadGameEnd(mPresenter.getGameBeanGameId());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null != mShakeUtils) {
            mShakeUtils.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (null != mShakeUtils) {
            mShakeUtils.onPause();
        }
    }

    @Override
    public void onShake() {
        Timber.i("摇一摇");
    }

    @Override
    public void onGameOverDialogDismiss() {
        finish();
    }
}
