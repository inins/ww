package com.wang.social.moneytree.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.base.BaseFragment;
import com.frame.component.common.AppConstant;
import com.frame.component.entities.User;
import com.frame.component.enums.ConversationType;
import com.frame.component.enums.ShareSource;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.CommonHelper;
import com.frame.component.router.Router;
import com.frame.component.router.ui.UIRouter;
import com.frame.component.service.im.ImService;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.utils.SpannableStringUtil;
import com.frame.component.view.DialogPay;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.integration.AppManager;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.SizeUtils;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.ToastUtil;
import com.umeng.socialize.UMShareAPI;
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
import com.wang.social.moneytree.mvp.ui.widget.DialogGameEnd;
import com.wang.social.moneytree.mvp.ui.widget.DialogShaked;
import com.wang.social.moneytree.mvp.ui.widget.MoneyTreeView;
import com.wang.social.moneytree.utils.Keys;
import com.wang.social.moneytree.utils.ShakeUtils;
import com.wang.social.socialize.SocializeUtil;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static com.wang.social.moneytree.utils.Keys.NAME_GAME_BEAN;
import static com.wang.social.moneytree.utils.Keys.NAME_GAME_RECORD;
import static com.wang.social.moneytree.utils.Keys.NAME_GAME_TYPE;
import static com.wang.social.moneytree.utils.Keys.TYPE_FROM_SQUARE;

@RouteNode(path = "/money_tree_room", desc = "游戏房间")
public class GameRoomActivity extends BaseAppActivity<GameRoomPresenter>
        implements GameRoomContract.View, CountDownTextView.CountDownListener,
        ShakeUtils.OnShakeListener, DialogGameEnd.GameOverListener,
        GameRoomMemberListAdapter.ClickListener {

    public static void startGame(Context context, GameBean gameBean, @Keys.GameType int type) {
        Intent intent = new Intent(context, GameRoomActivity.class);
        intent.putExtra(NAME_GAME_TYPE, type);
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
    @BindView(R2.id.money_tree_layout)
    MoneyTreeView mMoneyTreeView;
    @BindView(R2.id.member_chat_layout)
    LinearLayout mMemberChatLayout;

    private ShakeUtils mShakeUtils;
    @Inject
    AppManager mAppManager;
    private boolean mResumed = false;

    private boolean mGameEnded = false;
    private int mShakeCount = 0;

    // 聊天模块是否初始化
    private boolean mChatFramgentInited = false;

    private @Keys.GameType
    int mType;

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

        // 从哪里创建的游戏
        mType = getIntent().getIntExtra(NAME_GAME_TYPE, TYPE_FROM_SQUARE);
        // 游戏信息
        mPresenter.setGameBean(getIntent().getParcelableExtra(NAME_GAME_BEAN));
        // 游戏记录信息
        mPresenter.setGameRecord(getIntent().getParcelableExtra(NAME_GAME_RECORD));
        int roomId = getIntent().getIntExtra(Keys.NAME_ROOM_ID, -1);
        if (roomId > 0 && null == mPresenter.getGameBean()) {
            // 重设GameBean
            GameBean gameBean = new GameBean();
            gameBean.setRoomId(roomId);
            mPresenter.setGameBean(gameBean);
        }

        mToolbar.setOnButtonClickListener(clickType -> {
            if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                finish();
            }
        });

        // 摇钱树动画结束回调
        mMoneyTreeView.setAnimCallback(() -> {
            if (null != mPresenter.getGameBean() && mPresenter.getGameBean().getIsJoined() == 1 && !mGameEnded) {
                DialogShaked.show(getSupportFragmentManager())
                        .setCallback(new DialogShaked.Callback() {
                            @Override
                            public void onResume() {
//                                mShakeHintTV.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onDestroy() {
//                                mShakeHintTV.setText(getString(R.string.mt_shaked_hint));
//                                mShakeHintTV.setVisibility(View.VISIBLE);
                            }
                        });
            }
        });

        if (mPresenter.getRoomId() > 0) {
            ImService imService = (ImService) Router.getInstance().getService(ImService.class.getName());
            mChatFragment = imService.getGameConversationFragment(Integer.toString(mPresenter.getRoomId()));
            // 聊天
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.chat_layout,
                            mChatFragment)
                    .commit();
        }

        // 游戏房间
        if (null != mPresenter.getGameBean()) {
            mMoneyTreeView.setClickable(true);
            // 成员列表
            mMemberAdapter = new GameRoomMemberListAdapter(mMemberListRV, mPresenter.getMemberList());
            mMemberAdapter.setClickListener(this);
            mMemberListRV.setLayoutManager(
                    new LinearLayoutManager(
                            this,
                            LinearLayoutManager.HORIZONTAL,
                            false));
            mMemberListRV.setAdapter(mMemberAdapter);

            mPresenter.getRoomMsg(mPresenter.getRoomId());
        } else if (null != mPresenter.getGameRecord()) {
            // 游戏记录
            mMoneyTreeView.setClickable(false);
            // 显示地上钻石
            mMoneyTreeView.showGroundDiamond(true);

            mGameScoreAdapter = new GameScoreAdapter(mMemberListRV, mPresenter.getGameScoreList());
            // 游戏结果列表距离顶部的距离有变化
            LinearLayout.LayoutParams listParam = (LinearLayout.LayoutParams) mMemberListRV.getLayoutParams();
            listParam.topMargin = SizeUtils.dp2px(50);
            mMemberListRV.setLayoutParams(listParam);

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

    /**
     * 游戏记录加载完成
     */
    @Override
    public void onGetRecordDetailSuccess(GameRecordDetail recordDetail) {
        // 隐藏
        mCountDownLayout.setVisibility(View.GONE);
        mJoinNumLayout.setVisibility(View.GONE);

        // 是否显示 进入群聊 按钮
        showGoChat(recordDetail.getGroupId() > 0);

//        mGameRuleIV.setVisibility(View.INVISIBLE);
//        mGameRuleIV.setClickable(false);
        mJoinGameIV.setVisibility(View.GONE);
//        mShakeHintTV.setVisibility(View.GONE);

        // 房间名
        mTitleTV.setText(recordDetail.getRoomName());

        if (null != mGameScoreAdapter) {
            mGameScoreAdapter.notifyDataSetChanged();
        }

        // 显示
        mContentLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 游戏结果加载完成
     */
    @Override
    public void onLoadGameEndSuccess(GameEnd gameEnd) {
        // 显示对话框
        DialogGameEnd.show(getSupportFragmentManager(), gameEnd, this);
    }

    private BaseFragment mChatFragment;

    /**
     * 控制进入趣聊按钮显示
     *
     * @param visible 是否显示
     */
    private void showGoChat(boolean visible) {
        mGoChatIV.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        mGoChatIV.setClickable(visible);
    }

    /**
     * 游戏房间信息加载完成
     */
    @Override
    public void onGetRoomMsgSuccess(RoomMsg roomMsg) {
        // 如果群id有效，则显示 进入趣聊 按钮
        showGoChat(roomMsg.getGroupId() > 0);
//        if (mType != Keys.TYPE_FROM_GROUP) {
//            mGoChatIV.setVisibility(View.INVISIBLE);
//            mGoChatIV.setClickable(false);
//        }

        // 房间名
        mTitleTV.setText(roomMsg.getRoomName());
        // 倒计时
        mCountDownTV.start(roomMsg.getResetTime());
        mCountDownTV.setCountDownListener(this);
        // xx人参与中
        setJoinNumTV(roomMsg.getJoinNum());
        // 累计xx钻
        mDiamondNumTV.setText(String.format(getString(R.string.mt_format_game_room_diamond_num), roomMsg.getTotalDiamond()));
        // 是否已加入游戏
        // :isJoin:是否加入该游戏(0：未加入，1:加入);
        if (roomMsg.getIsJoin() == 0) {
            // 未加入游戏
            mJoinGameIV.setVisibility(View.VISIBLE);
        } else {
            // 已加入游戏
            mJoinGameIV.setVisibility(View.GONE);

            // 已加入游戏，显示地上钻石
            mMoneyTreeView.showGroundDiamond(true);

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
        DialogPay.showPayDiamond(this,
                getSupportFragmentManager(),
                titleText,
                getString(R.string.mt_format_pay_hint),
                getString(R.string.common_cancel),
                getString(R.string.mt_pay_immediately),
                getString(R.string.mt_recharge_immediately),
                joinGame.getDiamond(),
                joinGame.getBalance(),
                () -> mPresenter.payJoinGame(joinGame));
    }

    @Override
    public void onPayJoinGameSuccess() {
        // 加入游戏支付成功
        ToastUtil.showToastLong("参与成功");

        if (null != mPresenter.getGameBean()) {
            mPresenter.getGameBean().setIsJoined(1);
        }

        // 刷新游戏
        mPresenter.getRoomMsg(mPresenter.getRoomId());

        // 播放钻石落下动画
        mMoneyTreeView.startAnim(true);

        // 通知游戏列表刷新(通过IM发送的消息来刷新)
    }

    @Override
    public void onLoadMemberListSuccess() {
        if (null != mPresenter.getMemberList()) {
            // 有一个时更多图标，所以-1
            setJoinNumTV(mPresenter.getMemberList().size() - 1);
        }
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
     * 进入趣聊
     */
    @OnClick(R2.id.go_chat_image_view)
    public void goToChat() {
//        mAppManager.killActivity(GameListActivity.class);
//        finish();
        if (mPresenter.isGroupMember()) {
            // 是群成员，进入趣聊
            CommonHelper.ImHelper.gotoGroupConversation(
                    this,
                    Integer.toString(mPresenter.getGroupId()),
                    ConversationType.SOCIAL,
                    false);
        } else {
            // 不是群成员，进入群详情
            CommonHelper.ImHelper.startGroupInviteBrowse(this, mPresenter.getGroupId());
        }
    }

    /**
     * 游戏规则
     */
    @OnClick(R2.id.game_rule_image_view)
    public void goToGameRule() {
        WebActivity.start(this,
                AppConstant.Url.cashcowrule);
    }

    /**
     * 游戏列表
     */
    @OnClick(R2.id.game_list_image_view)
    public void goToGameList() {
//        mAppManager.killActivity(GameRecordListActivity.class);
//        finish();
//        if (mPresenter.getGroupId() > 0) {
//            GameListActivity.startFromGroup(this, mPresenter.getGroupId());
//        } else {
//            GameListActivity.startFromSquare(this);
//        }

        GameListActivity.startFromSquare(this);
    }

    @Override
    public void onCountDownFinished() {
    }

    private View.OnLayoutChangeListener mLayoutChangeListener =
            (View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) -> {
                if (v.getHeight() > v.getMinimumHeight()) {
                    mJoinNumLayout.setVisibility(View.INVISIBLE);
//                    mShakeHintTV.setVisibility(View.INVISIBLE);
                } else {
                    mJoinNumLayout.setVisibility(View.VISIBLE);
//                    mShakeHintTV.setVisibility(View.VISIBLE);
                }
            };

    @Override
    protected void onResume() {
        super.onResume();
        mResumed = true;

        if (null != mShakeUtils) {
            mShakeUtils.onResume();
        }

        if (null != mPresenter.getGameBean() && mPresenter.isJoined()) {
            mMemberChatLayout.addOnLayoutChangeListener(mLayoutChangeListener);
        }

        // 如果游戏已经结束，则加载游戏结果
        if (mGameEnded) {
            mPresenter.loadGameEnd(mPresenter.getGameId());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mResumed = false;

        if (null != mShakeUtils) {
            mShakeUtils.onPause();
        }

        mMemberChatLayout.removeOnLayoutChangeListener(mLayoutChangeListener);
    }

    @Override
    public void onShake() {
        if (!mMoneyTreeView.isClickable()) return;

        Timber.i("摇一摇");
        // 模拟摇钱树点击
        if (++mShakeCount > 3) {
            mShakeCount = 0;
            setSimulateClick(mMoneyTreeView, mMoneyTreeView.getX(), mMoneyTreeView.getY());
        }
    }

    private void setSimulateClick(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

    @Override
    public void onGameOverDialogDismiss() {
        finish();
    }

    @Override
    public void onMemberMore() {
        MemberListActivity.start(this, mPresenter.getGameId());
    }

    @OnClick(R2.id.share_image_view)
    public void share() {
        User loginUser = AppDataHelper.getUser();
        String shareUrl = String.format(AppConstant.Share.SHARE_GAME_TREE_URL, String.valueOf(loginUser.getUserId()), String.valueOf(mPresenter.getRoomId()));
        String shareContent = String.format(AppConstant.Share.SHARE_GAME_TREE_CONTENT, loginUser.getNickname());
        SocializeUtil.shareWithWW(getSupportFragmentManager(),
                null,
                shareUrl,
                AppConstant.Share.SHARE_GAME_TREE_TITLE,
                shareContent,
                AppConstant.Share.SHARE_GAME_DEFAULT_IMAGE,
                (String url, String title, String content, String imageUrl) -> {
                    CommonHelper.ImHelper.startWangWangShare(this,
                            "",
                            Integer.toString(mPresenter.getRoomMsg().getDiamond()),
                            "",
                            ShareSource.SOURCE_GAME_TREE,
                            Integer.toString(mPresenter.getRoomId()));
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (null == mChatFragment) {
            super.onBackPressed();
        } else {
            if (mChatFragment.onBackPressed()) {
                resetMemberChatLayoutHeight(0);
            } else {
                super.onBackPressed();
            }
        }
    }

    private void resetMemberChatLayoutHeight(int height) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mMemberChatLayout.getLayoutParams();
        if (height > 0) {
            params.height = mMemberChatLayout.getMinimumHeight() + height;
            mMemberChatLayout.setLayoutParams(params);
        } else {
            params.height = mMemberChatLayout.getMinimumHeight();
            mMemberChatLayout.setLayoutParams(params);
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        super.onCommonEvent(event);

        switch (event.getEvent()) {
            // 表情输入框弹出
            case EventBean.EVENT_GAME_INPUT_HEIGHT_CHANGED:
                int height = (int) event.get("height");
                resetMemberChatLayoutHeight(height);
                break;
            case EventBean.EVENT_GAME_JOIN:
                Timber.i("用户加入游戏");
                int number = (int) event.get("joinNumber");
                // 重新加载房间信息
                mPresenter.getRoomMsg(mPresenter.getRoomId());
                // 重新加载成员列表
                mPresenter.loadMemberList();
                break;
            case EventBean.EVENT_GAME_RESULT:
//                if (!mResumed) return;
                Timber.i("游戏结束");
                gameEnd();
                break;
        }
    }

    private void gameEnd() {
        mGameEnded = true;
        mMoneyTreeView.setClickable(false);
//        mShakeHintTV.setVisibility(View.GONE);
        // 游戏结束，加载结果
        if (mResumed) {
            mPresenter.loadGameEnd(mPresenter.getGameId());
        }
    }
}

