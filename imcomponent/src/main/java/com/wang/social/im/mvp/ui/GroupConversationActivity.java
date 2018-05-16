package com.wang.social.im.mvp.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.frame.component.path.ImPath;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.Autowired;
import com.frame.router.facade.annotation.RouteNode;
import com.wang.social.im.R;
import com.frame.component.enums.ConversationType;
import com.wang.social.im.mvp.contract.GroupConversationContract;
import com.wang.social.im.mvp.presenter.GroupConversationPresenter;
import com.wang.social.im.mvp.ui.fragments.SocialConversationFragment;
import com.wang.social.im.mvp.ui.fragments.TeamConversationFragment;
import com.wang.social.im.view.drawer.SlidingRootNav;
import com.wang.social.im.view.drawer.SlidingRootNavBuilder;

/**
 * 趣聊/觅聊会话界面
 */
@RouteNode(path = ImPath.GROUP_PATH, desc = "群（趣聊/觅聊）会话")
public class GroupConversationActivity extends BaseAppActivity<GroupConversationPresenter> implements GroupConversationContract.View {

    @Autowired
    String targetId;
    @Autowired
    int typeOrdinal;

    private SlidingRootNav mRootNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

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
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
}