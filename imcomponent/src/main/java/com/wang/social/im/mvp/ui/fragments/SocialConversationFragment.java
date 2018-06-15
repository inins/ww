package com.wang.social.im.mvp.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.frame.component.enums.ConversationType;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.imageloader.ImageLoader;
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
import com.wang.social.im.mvp.ui.SocialHomeActivity;

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
 * Create by ChenJing on 2018-05-15 11:21
 * ============================================
 */
public class SocialConversationFragment extends BaseConversationFragment {

    @BindView(R2.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R2.id.sc_tv_title)
    TextView scTvTitle;
    //    @BindView(R2.id.sc_tv_online)
//    TextView scTvOnline;
    @BindView(R2.id.background)
    ImageView background;
    @BindView(R2.id.sc_scroll)
    ScrollView scScroll;

    String targetId;

    @Inject
    ImageLoader mImageLoader;

    public static SocialConversationFragment newInstance(String targetId) {
        Bundle args = new Bundle();
        if (!targetId.startsWith(IMConstants.IM_IDENTITY_PREFIX_SOCIAL)) {
            targetId = IMConstants.IM_IDENTITY_PREFIX_SOCIAL + targetId;
        }
        args.putString("targetId", targetId);
        SocialConversationFragment fragment = new SocialConversationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        targetId = getArguments().getString("targetId");
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
        return R.layout.im_ac_social_conversation;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setListener();

        loadBackground(ConversationType.SOCIAL, targetId, background, mImageLoader);

        GroupProfile profile = GroupHelper.getInstance().getGroupProfile(targetId);
        if (profile != null) {
            scTvTitle.setText(profile.getName());
        } else {
            TIMGroupManagerExt.getInstance().getGroupDetailInfo(Arrays.asList(targetId), new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
                    for (TIMGroupDetailInfo info : timGroupDetailInfos) {
                        if (info.getGroupId().equals(targetId) && scTvTitle != null) {
                            scTvTitle.setText(info.getGroupName());
//                            scTvOnline.setText(UIUtil.getString(R.string.im_online_number, info.getOnlineMemberNum()));
                        }
                    }
                }
            });
        }

        ConversationFragment conversationFragment = ConversationFragment.newInstance(ConversationType.SOCIAL, targetId);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.sc_fl_conversation, conversationFragment, ConversationFragment.class.getName() + "social");
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
                        SocialHomeActivity.start(getContext(), ImHelper.imId2WangId(targetId));
                        break;
                }
            }
        });
        scScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //禁止滑动
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getChildFragmentManager().findFragmentByTag(ConversationFragment.class.getName() + "social");
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
            if (ConversationType.values()[Integer.valueOf(event.get("typeOrdinal").toString())] == ConversationType.SOCIAL &&
                    targetId.equals(event.get("targetId").toString())) {
                loadBackground(ConversationType.SOCIAL, targetId, background, mImageLoader);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupProfileChanged(TIMGroupCacheInfo timGroupCacheInfo) {
        if (timGroupCacheInfo != null && timGroupCacheInfo.getGroupInfo().getGroupId().equals(targetId)) {
            scTvTitle.setText(timGroupCacheInfo.getGroupInfo().getGroupName());
        }
    }
}
