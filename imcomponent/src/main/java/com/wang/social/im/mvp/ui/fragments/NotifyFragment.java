package com.wang.social.im.mvp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.BasicFragment;
import com.frame.di.component.AppComponent;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.ui.FindChatRequestListActivity;
import com.wang.social.im.mvp.ui.FriendRequestListActivity;
import com.wang.social.im.mvp.ui.SysMsgListActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * ============================================
 * 通知
 * <p>
 * Create by ChenJing on 2018-05-07 19:56
 * ============================================
 */
public class NotifyFragment extends BasicFragment {

    public static NotifyFragment newInstance() {
        Bundle args = new Bundle();
        NotifyFragment fragment = new NotifyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_fragment_notify;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @OnClick({R2.id.lay_official, R2.id.text_sysmsg, R2.id.text_friend, R2.id.text_findchat, R2.id.text_funchat, R2.id.text_zan, R2.id.text_eva, R2.id.text_aite})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.lay_official) {

        } else if (id == R.id.text_sysmsg) {
            SysMsgListActivity.start(getContext());
        } else if (id == R.id.text_friend) {
            FriendRequestListActivity.start(getContext());
        } else if (id == R.id.text_findchat) {
            FindChatRequestListActivity.start(getContext());
        } else if (id == R.id.text_funchat) {

        } else if (id == R.id.text_zan) {

        } else if (id == R.id.text_eva) {

        } else if (id == R.id.text_aite) {

        }
    }
}
