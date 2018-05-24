package com.wang.social.im.mvp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frame.base.BasicFragment;
import com.frame.component.helper.CommonHelper;
import com.frame.di.component.AppComponent;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.ui.NofityFriendRequestListActivity;
import com.wang.social.im.mvp.ui.NotifyAiteListActivity;
import com.wang.social.im.mvp.ui.NotifyEvaListActivity;
import com.wang.social.im.mvp.ui.NotifyFindChatRequestListActivity;
import com.wang.social.im.mvp.ui.NotifyGroupRequestListActivity;
import com.wang.social.im.mvp.ui.NotifySysMsgListActivity;
import com.wang.social.im.mvp.ui.NotifyZanListActivity;

import butterknife.BindView;
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

    @BindView(R2.id.img_dot_notify_sys)
    ImageView imgDotNotifySys;
    @BindView(R2.id.img_dot_notify_frient)
    ImageView imgDotNotifyFrient;
    @BindView(R2.id.img_dot_notify_findchat)
    ImageView imgDotNotifyFindchat;
    @BindView(R2.id.img_dot_notify_funchat)
    ImageView imgDotNotifyFunchat;
    @BindView(R2.id.img_dot_notify_zan)
    ImageView imgDotNotifyZan;
    @BindView(R2.id.img_dot_notify_eva)
    ImageView imgDotNotifyEva;
    @BindView(R2.id.img_dot_notify_aite)
    ImageView imgDotNotifyAite;

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
            CommonHelper.ImHelper.startOfficialChatRobot(getContext());
        } else if (id == R.id.text_sysmsg) {
            NotifySysMsgListActivity.start(getContext());
            imgDotNotifySys.setVisibility(View.GONE);
        } else if (id == R.id.text_friend) {
            NofityFriendRequestListActivity.start(getContext());
            imgDotNotifyFrient.setVisibility(View.GONE);
        } else if (id == R.id.text_findchat) {
            NotifyFindChatRequestListActivity.start(getContext());
            imgDotNotifyFindchat.setVisibility(View.GONE);
        } else if (id == R.id.text_funchat) {
            NotifyGroupRequestListActivity.start(getContext());
            imgDotNotifyFunchat.setVisibility(View.GONE);
        } else if (id == R.id.text_zan) {
            NotifyZanListActivity.start(getContext());
            imgDotNotifyZan.setVisibility(View.GONE);
        } else if (id == R.id.text_eva) {
            NotifyEvaListActivity.start(getContext());
            imgDotNotifyEva.setVisibility(View.GONE);
        } else if (id == R.id.text_aite) {
            NotifyAiteListActivity.start(getContext());
            imgDotNotifyAite.setVisibility(View.GONE);
        }
    }
}
