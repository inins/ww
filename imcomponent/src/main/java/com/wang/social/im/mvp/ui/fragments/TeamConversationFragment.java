package com.wang.social.im.mvp.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.frame.component.enums.ConversationType;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.imageloader.ImageLoader;
import com.frame.utils.BarUtils;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.di.component.DaggerFragmentComponent;
import com.wang.social.im.helper.GroupHelper;
import com.wang.social.im.helper.ImHelper;
import com.wang.social.im.mvp.model.entities.GroupProfile;
import com.wang.social.im.mvp.ui.ConversationFragment;
import com.wang.social.im.mvp.ui.GroupConversationActivity;
import com.wang.social.im.mvp.ui.TeamHomeActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-15 14:10
 * ============================================
 */
public class TeamConversationFragment extends BaseConversationFragment {

    @BindView(R2.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R2.id.tc_tv_title)
    TextView tcTvTitle;
    //    @BindView(R2.id.tc_tv_online)
//    TextView tcTvOnline;
    @BindView(R2.id.background)
    ImageView ivBackground;
    @BindView(R2.id.tc_fl_toolbar)
    FrameLayout flToolbar;
    @BindView(R2.id.tc_scroll)
    NestedScrollView tcScroll;

    private String targetId;

    @Inject
    ImageLoader mImageLoader;

    public static TeamConversationFragment newInstance(String targetId) {
        Bundle args = new Bundle();
        if (!targetId.startsWith(IMConstants.IM_IDENTITY_PREFIX_TEAM)) {
            targetId = IMConstants.IM_IDENTITY_PREFIX_TEAM + targetId;
        }
        args.putString("targetId", targetId);
        TeamConversationFragment fragment = new TeamConversationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        targetId = getArguments().getString("targetId");

        BarUtils.setTranslucentForImageView(getActivity(), 0, null);
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerFragmentComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_ac_team_conversation;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            flToolbar.setPadding(0, BarUtils.getStatusBarHeight(getContext()), 0, 0);
        }

        setListener();

        loadBackground(ConversationType.TEAM, targetId, ivBackground, mImageLoader);

        GroupProfile profile = GroupHelper.getInstance().getGroupProfile(targetId);
        if (profile != null) {
            tcTvTitle.setText(profile.getName());
        } else {
            TIMGroupManagerExt.getInstance().getGroupDetailInfo(Arrays.asList(targetId), new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
                    for (TIMGroupDetailInfo info : timGroupDetailInfos) {
                        if (info.getGroupId().equals(targetId) && tcTvTitle != null) {
                            tcTvTitle.setText(info.getGroupName());
//                        tcTvOnline.setText(UIUtil.getString(R.string.im_online_total_number, info.getMemberNum(), info.getOnlineMemberNum()));
                        }
                    }
                }
            });
        }

        ConversationFragment conversationFragment = ConversationFragment.newInstance(ConversationType.TEAM, targetId);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.tc_fl_conversation, conversationFragment, ConversationFragment.class.getName() + "team");
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        getActivity().onBackPressed();
                        break;
                    case RIGHT_ICON:
                        String mirrorId = targetId.replace(IMConstants.IM_IDENTITY_PREFIX_TEAM, IMConstants.IM_IDENTITY_PREFIX_MIRROR);
                        ((GroupConversationActivity) getActivity()).gotoMirror(mirrorId);
                        break;
                }
            }
        });
        tcScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @OnClick({R2.id.tc_fl_more, R2.id.tc_fl_mirror, R2.id.tc_tv_title})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.tc_fl_more || view.getId() == R.id.tc_tv_title) {
            TeamHomeActivity.start(getContext(), ImHelper.imId2WangId(targetId));
        } else if (view.getId() == R.id.tc_fl_mirror) {
            String mirrorId = targetId.replace(IMConstants.IM_IDENTITY_PREFIX_TEAM, IMConstants.IM_IDENTITY_PREFIX_MIRROR);
            ((GroupConversationActivity) getActivity()).gotoMirror(mirrorId);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getChildFragmentManager().findFragmentByTag(ConversationFragment.class.getName() + "team");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        if (event.getEvent() == EventBean.EVENT_NOTIFY_BACKGROUND) {
            if (ConversationType.values()[Integer.valueOf(event.get("typeOrdinal").toString())] == ConversationType.TEAM &&
                    targetId.equals(event.get("targetId").toString())) {
                loadBackground(ConversationType.TEAM, targetId, ivBackground, mImageLoader);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupProfileChanged(TIMGroupCacheInfo timGroupCacheInfo) {
        if (timGroupCacheInfo != null && timGroupCacheInfo.getGroupInfo().getGroupId().equals(targetId)) {
            tcTvTitle.setText(timGroupCacheInfo.getGroupInfo().getGroupName());
        }
    }
}
