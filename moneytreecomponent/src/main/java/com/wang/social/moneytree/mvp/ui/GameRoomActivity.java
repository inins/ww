package com.wang.social.moneytree.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.base.BaseFragment;
import com.frame.component.router.Router;
import com.frame.component.router.ui.UIRouter;
import com.frame.component.service.funpoint.FunpointService;
import com.frame.component.service.im.ImService;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.utils.SpannableStringUtil;
import com.frame.component.view.DialogPay;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.integration.AppManager;
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

    // 是否已经摇过了
    private boolean mShaked = false;
    private int mShakeCount = 0;

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

        mToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    finish();
                }
            }
        });

        mMoneyTreeView.setAnimCallback(new MoneyTreeView.AnimCallback() {
            @Override
            public void onAnimEnd() {
                if (null != mPresenter.getGameBean() && !mShaked) {
                    mShaked = true;
                    DialogShaked.show(getSupportFragmentManager())
                            .setCallback(new DialogShaked.Callback() {
                                @Override
                                public void onResume() {
                                    mShakeHintTV.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onDestroy() {
                                    mShakeHintTV.setText(getString(R.string.mt_shaked_hint));
                                    mShakeHintTV.setVisibility(View.VISIBLE);
                                }
                            });
                }
            }
        });

        // 游戏房间
        if (null != mPresenter.getGameBean()) {
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

    /**
     * 游戏记录加载完成
     */
    @Override
    public void onGetRecordDetailSuccess(GameRecordDetail recordDetail) {
        // 隐藏
        mCountDownLayout.setVisibility(View.GONE);
        mJoinNumLayout.setVisibility(View.GONE);
        mGoChatIV.setVisibility(View.INVISIBLE);
        mGoChatIV.setClickable(false);
        mGameRuleIV.setVisibility(View.INVISIBLE);
        mGameRuleIV.setClickable(false);
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
     * 游戏房间信息加载完成
     */
    @Override
    public void onGetRoomMsgSuccess(RoomMsg roomMsg) {
        // 如果不是从群里创建的游戏，则不显示 进入聊天
        if (mType != Keys.TYPE_FROM_GROUP) {
            mGoChatIV.setVisibility(View.INVISIBLE);
            mGoChatIV.setClickable(false);
        }

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

            // 加载成员列表
            mPresenter.loadMemberList();
        }

        ImService imService = (ImService) Router.getInstance().getService(ImService.class.getName());
        mChatFragment = imService.getGameConversationFragment(Integer.toString(mPresenter.getRoomId()));
        // 聊天
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.chat_layout,
                        mChatFragment)
                .commit();

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
                () -> mPresenter.payJoinGame(joinGame));
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
        if (null != mPresenter.getMemberList()) {
            setJoinNumTV(mPresenter.getMemberList().size());
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
     * 去聊
     */
    @OnClick(R2.id.go_chat_image_view)
    public void goToChat() {
        mAppManager.killActivity(GameListActivity.class);
        finish();
    }

    /**
     * 游戏规则
     */
    @OnClick(R2.id.game_rule_image_view)
    public void goToGameRule() {
        WebActivity.start(this,
                "http://wangsocial.com/share/v_2.0/test/contentShared/cashcow/rule.html");
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
    }

    private View.OnLayoutChangeListener mLayoutChangeListener =
            (View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) -> {
                if (v.getHeight() > v.getMinimumHeight()) {
                    mJoinNumLayout.setVisibility(View.INVISIBLE);
                    mShakeHintTV.setVisibility(View.INVISIBLE);
                } else {
                    mJoinNumLayout.setVisibility(View.VISIBLE);
                    mShakeHintTV.setVisibility(View.VISIBLE);
                }
            };
    @Override
    protected void onResume() {
        super.onResume();
        mResumed = true;

        if (null != mShakeUtils) {
            mShakeUtils.onResume();
        }

        mMemberChatLayout.addOnLayoutChangeListener(mLayoutChangeListener);
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
        if (++mShakeCount > 5) {
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
        MemberListActivity.start(this, mPresenter.getGameBeanGameId());
    }

    @OnClick(R2.id.share_image_view)
    public void share() {
        SocializeUtil.shareWithWW(getSupportFragmentManager(),
                null,
                "http://www.wangsocial.com/",
                "往往",
                "有点2的社交软件",
                "http://resouce.dongdongwedding.com/activity_cashcow_moneyTree.png",
                new SocializeUtil.WWShareListener() {
                    @Override
                    public void onWWShare(String url, String title, String content, String imageUrl) {
                        showToastLong("往往分享");
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        boolean flag = mChatFragment.onBackPressed();
        if (null == mChatFragment || !flag) {
            super.onBackPressed();
        }

        if (flag) {
            resetMemberChatLayoutHeight(0);
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
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
                mPresenter.loadMemberList();
                break;
            case EventBean.EVENT_GAME_RESULT:
                if (!mResumed) return;
                Timber.i("游戏结束");
                // 游戏结束，加载结果
                mPresenter.loadGameEnd(mPresenter.getGameBeanGameId());
                break;
        }
    }


}

