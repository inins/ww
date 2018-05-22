package com.wang.social.im.mvp.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.frame.base.BaseAdapter;
import com.frame.component.enums.ConversationType;
import com.frame.component.path.ImPath;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.dialog.PayDialog;
import com.frame.component.utils.UIUtil;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.router.facade.annotation.Autowired;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.SizeUtils;
import com.wang.social.im.R;
import com.wang.social.im.di.component.DaggerGroupConversationComponent;
import com.wang.social.im.di.modules.GroupConversationModule;
import com.wang.social.im.helper.ImHelper;
import com.wang.social.im.mvp.contract.GroupConversationContract;
import com.wang.social.im.mvp.model.entities.GroupJoinCheckResult;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.presenter.GroupConversationPresenter;
import com.wang.social.im.mvp.ui.adapters.JoinedTeamListAdapter;
import com.wang.social.im.mvp.ui.adapters.TeamListAdapter;
import com.wang.social.im.mvp.ui.fragments.SocialConversationFragment;
import com.wang.social.im.mvp.ui.fragments.TeamConversationFragment;
import com.wang.social.im.utils.ActivitySwitcher;
import com.wang.social.im.view.drawer.SlidingRootNav;
import com.wang.social.im.view.drawer.SlidingRootNavBuilder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 趣聊/觅聊会话界面
 */
@RouteNode(path = ImPath.GROUP_PATH, desc = "群（趣聊/觅聊）会话")
public class GroupConversationActivity extends BaseAppActivity<GroupConversationPresenter> implements GroupConversationContract.View, View.OnClickListener, BaseAdapter.OnItemClickListener<TeamInfo>, TeamListAdapter.OnJoinClickListener {

    private TextView tvbJoinedCreate, tvbListCreate;

    @Autowired
    String targetId;
    @Autowired
    int typeOrdinal;
    @Autowired
    boolean fromMirror;

    private SlidingRootNav mRootNav;

    private boolean mCreate;

    private TeamListAdapter mAllTeamsAdapter;
    private JoinedTeamListAdapter mJoinTeamsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mCreate = true;
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGroupConversationComponent
                .builder()
                .appComponent(appComponent)
                .groupConversationModule(new GroupConversationModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_group_conversation;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        ConversationType conversationType = ConversationType.values()[typeOrdinal];

        init(savedInstanceState, conversationType);

        showFragment(targetId, conversationType);

        mPresenter.getMiList(ImHelper.imId2WangId(targetId));
        mPresenter.getSelfMiList(ImHelper.imId2WangId(targetId));
    }

    private void init(Bundle savedInstanceState, ConversationType conversationType) {
        mRootNav = new SlidingRootNavBuilder(this)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.im_view_gcv_drawer)
                .inject();

        if (conversationType != ConversationType.SOCIAL) {
            mRootNav.setMenuLocked(true);
        }

        tvbJoinedCreate = findViewById(R.id.gd_joined_create);
        tvbListCreate = findViewById(R.id.gd_list_create);
        tvbJoinedCreate.setOnClickListener(this);
        tvbListCreate.setOnClickListener(this);

        RecyclerView rlvJoinTeams = findViewById(R.id.gd_joined_list);
        RecyclerView rlvAllTeams = findViewById(R.id.gd_team_list);

        rlvJoinTeams.setLayoutManager(new LinearLayoutManager(this));
        rlvJoinTeams.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = SizeUtils.dp2px(3);
            }
        });
        mJoinTeamsAdapter = new JoinedTeamListAdapter();
        mJoinTeamsAdapter.setOnItemClickListener(this);
        rlvJoinTeams.setAdapter(mJoinTeamsAdapter);

        rlvAllTeams.setLayoutManager(new LinearLayoutManager(this));
        rlvAllTeams.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = SizeUtils.dp2px(8);
            }
        });
        mAllTeamsAdapter = new TeamListAdapter(this);
        mAllTeamsAdapter.setOnItemClickListener(this);
        rlvAllTeams.setAdapter(mAllTeamsAdapter);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    private void showFragment(String targetId, ConversationType conversationType) {
        Fragment fragment;
        if (conversationType == ConversationType.SOCIAL) {
            fragment = SocialConversationFragment.newInstance(targetId);
        } else {
            fragment = TeamConversationFragment.newInstance(targetId);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.gc_fl_content, fragment, fragment.getClass().getName());
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.gc_fl_content);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void gotoMirror(String id) {
        Intent intent = new Intent(this, MirrorConversationActivity.class);
        intent.putExtra("targetId", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        ActivitySwitcher.animationOut(findViewById(R.id.im_root_layout), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onResume() {
        if (mCreate && fromMirror) {
            ActivitySwitcher.animationIn(findViewById(R.id.im_root_layout), getWindowManager());
        }
        mCreate = false;
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.gd_joined_create || v.getId() == R.id.gd_list_create) {
            CreateTeamActivity.start(this, ImHelper.imId2WangId(targetId));
            mRootNav.closeMenu();
        }
    }

    @Override
    public void showAllTeams(List<TeamInfo> teams) {
        mAllTeamsAdapter.refreshData(teams);
    }

    @Override
    public void showSelfTeams(List<TeamInfo> teams) {
        mJoinTeamsAdapter.refreshData(teams);
    }

    @Override
    public void showPayDialog(GroupJoinCheckResult checkResult) {
        String message = UIUtil.getString(R.string.im_join_team_pay_tip, checkResult.getJoinCost());
        PayDialog dialog = new PayDialog(this, new PayDialog.OnPayListener() {
            @Override
            public void onPay() {
                mPresenter.payForJoin(ImHelper.imId2WangId(targetId), checkResult);
            }
        }, message, String.valueOf(checkResult.getJoinCost()));
        dialog.show();
    }

    @Override
    public void onItemClick(TeamInfo teamInfo, int position) {
        showFragment(teamInfo.getTeamId(), ConversationType.TEAM);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRootNav.closeMenu();
                mRootNav.setMenuLocked(true);
            }
        }, 150);
    }

    @Override
    public void onJoinClick(TeamInfo teamInfo) {
        mPresenter.checkJoinStatus(ImHelper.imId2WangId(targetId), teamInfo.getTeamId());
    }


    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        if (event.getEvent() == EventBean.EVENT_NOTIFY_CREATE_TEAM) {
            mPresenter.getSelfMiList(ImHelper.imId2WangId(targetId));
            mPresenter.getMiList(ImHelper.imId2WangId(targetId));
        }
    }
}