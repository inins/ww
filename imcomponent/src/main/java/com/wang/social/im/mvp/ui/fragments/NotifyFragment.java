package com.wang.social.im.mvp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frame.base.BasicFragment;
import com.frame.component.entities.DynamicMessage;
import com.frame.component.entities.SystemMessage;
import com.frame.component.entities.msg.NotifySave;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.MsgHelper;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.ui.NofityFriendRequestListActivity;
import com.wang.social.im.mvp.ui.NotifyAiteListActivity;
import com.wang.social.im.mvp.ui.NotifyEvaListActivity;
import com.wang.social.im.mvp.ui.NotifyFindChatRequestListActivity;
import com.wang.social.im.mvp.ui.NotifyGroupRequestListActivity;
import com.wang.social.im.mvp.ui.NotifySysMsgListActivity;
import com.wang.social.im.mvp.ui.NotifyZanListActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.http.GET;

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(SystemMessage msg) {
        switch (MsgHelper.getSysMsgCate(msg)) {
            case MsgHelper.CATE_SYS:
                imgDotNotifySys.setVisibility(View.VISIBLE);
                MsgHelper.modifyNotify(MsgHelper.CATE_SYS, true);
                break;
            case MsgHelper.CATE_FRIEND:
                imgDotNotifyFrient.setVisibility(View.VISIBLE);
                MsgHelper.modifyNotify(MsgHelper.CATE_FRIEND, true);
                break;
            case MsgHelper.CATE_GROUP:
                imgDotNotifyFindchat.setVisibility(View.VISIBLE);
                MsgHelper.modifyNotify(MsgHelper.CATE_GROUP, true);
                break;
            case MsgHelper.CATE_JOIN:
                imgDotNotifyFunchat.setVisibility(View.VISIBLE);
                MsgHelper.modifyNotify(MsgHelper.CATE_JOIN, true);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(DynamicMessage msg) {
        switch (MsgHelper.getDynMsgCate(msg)) {
            case MsgHelper.CATE_ZAN:
                imgDotNotifyZan.setVisibility(View.VISIBLE);
                MsgHelper.modifyNotify(MsgHelper.CATE_ZAN, true);
                break;
            case MsgHelper.CATE_EVA:
                imgDotNotifyEva.setVisibility(View.VISIBLE);
                MsgHelper.modifyNotify(MsgHelper.CATE_EVA, true);
                break;
            case MsgHelper.CATE_AITE:
                imgDotNotifyAite.setVisibility(View.VISIBLE);
                MsgHelper.modifyNotify(MsgHelper.CATE_AITE, true);
                break;
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
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
        NotifySave notifySave = AppDataHelper.getNotifySave();
        //回归已有消息提醒状态
        imgDotNotifySys.setVisibility(notifySave.getSysMsgCount() != 0 ? View.VISIBLE : View.GONE);
        imgDotNotifyFrient.setVisibility(notifySave.getFriendCount() != 0 ? View.VISIBLE : View.GONE);
        imgDotNotifyFindchat.setVisibility(notifySave.getGroupCount() != 0 ? View.VISIBLE : View.GONE);
        imgDotNotifyFunchat.setVisibility(notifySave.getJoinCount() != 0 ? View.VISIBLE : View.GONE);
        imgDotNotifyZan.setVisibility(notifySave.getZanCount() != 0 ? View.VISIBLE : View.GONE);
        imgDotNotifyEva.setVisibility(notifySave.getEvaCount() != 0 ? View.VISIBLE : View.GONE);
        imgDotNotifyAite.setVisibility(notifySave.getAiteCount() != 0 ? View.VISIBLE : View.GONE);
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
            MsgHelper.modifyNotify(MsgHelper.CATE_SYS, false);
        } else if (id == R.id.text_friend) {
            NofityFriendRequestListActivity.start(getContext());
            imgDotNotifyFrient.setVisibility(View.GONE);
            MsgHelper.modifyNotify(MsgHelper.CATE_FRIEND, false);
        } else if (id == R.id.text_findchat) {
            NotifyFindChatRequestListActivity.start(getContext());
            imgDotNotifyFindchat.setVisibility(View.GONE);
            MsgHelper.modifyNotify(MsgHelper.CATE_GROUP, false);
        } else if (id == R.id.text_funchat) {
            NotifyGroupRequestListActivity.start(getContext());
            imgDotNotifyFunchat.setVisibility(View.GONE);
            MsgHelper.modifyNotify(MsgHelper.CATE_JOIN, false);
        } else if (id == R.id.text_zan) {
            NotifyZanListActivity.start(getContext());
            imgDotNotifyZan.setVisibility(View.GONE);
            MsgHelper.modifyNotify(MsgHelper.CATE_ZAN, false);
        } else if (id == R.id.text_eva) {
            NotifyEvaListActivity.start(getContext());
            imgDotNotifyEva.setVisibility(View.GONE);
            MsgHelper.modifyNotify(MsgHelper.CATE_EVA, false);
        } else if (id == R.id.text_aite) {
            NotifyAiteListActivity.start(getContext());
            imgDotNotifyAite.setVisibility(View.GONE);
            MsgHelper.modifyNotify(MsgHelper.CATE_AITE, false);
        }
        checkHasNotifyAndPost();
    }

    //检查是否已经读完所有消息，并通知上级页面移除小红点
    private void checkHasNotifyAndPost() {
        if (MsgHelper.hasReadAllNotify()) {
            EventBus.getDefault().post(new EventBean(EventBean.EVENT_MSG_READALL));
        }
    }
}
