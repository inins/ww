package com.wang.social.im.mvp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.frame.component.enums.ConversationType;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.helper.GroupHelper;
import com.wang.social.im.mvp.model.entities.GroupProfile;
import com.wang.social.im.mvp.ui.ConversationFragment;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

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
    @BindView(R2.id.sc_tv_online)
    TextView scTvOnline;
    @BindView(R2.id.background)
    ImageView background;

    String targetId;

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

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_ac_social_conversation;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setListener();

        initBackground(ConversationType.SOCIAL, targetId, background);

        GroupProfile profile = GroupHelper.getInstance().getGroupProfile(targetId);
        if (profile != null) {
            scTvTitle.setText(profile.getName());
        }
        TIMGroupManagerExt.getInstance().getGroupDetailInfo(Arrays.asList(targetId), new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
                for (TIMGroupDetailInfo info : timGroupDetailInfos) {
                    if (info.getGroupId().equals(targetId) && scTvOnline != null) {
                        if (scTvTitle.getText().toString().isEmpty()) {
                            scTvTitle.setText(info.getGroupName());
                        }
                        scTvOnline.setText(UIUtil.getString(R.string.im_online_number, info.getOnlineMemberNum()));
                    }
                }
            }
        });

        ConversationFragment conversationFragment = ConversationFragment.newInstance(ConversationType.SOCIAL, targetId);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.sc_fl_conversation, conversationFragment, ConversationFragment.class.getName() + "social");
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    private void setListener() {
        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        getActivity().onBackPressed();
                        break;
                }
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
}
