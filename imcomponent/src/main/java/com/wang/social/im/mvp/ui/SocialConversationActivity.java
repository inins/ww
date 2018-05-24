package com.wang.social.im.mvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.path.ImPath;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.Autowired;
import com.frame.router.facade.annotation.RouteNode;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.frame.component.enums.ConversationType;
import com.wang.social.im.helper.GroupHelper;
import com.wang.social.im.mvp.model.entities.GroupProfile;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * ============================================
 * 趣聊会话界面
 * <p>
 * Create by ChenJing on 2018-04-23 11:51
 * ============================================
 */
@Deprecated
@RouteNode(path = ImPath.SOCIAL_PATH, desc = "趣聊会话")
public class SocialConversationActivity extends BasicConversationActivity {

    @BindView(R2.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R2.id.sc_tv_title)
    TextView scTvTitle;
    @BindView(R2.id.sc_tv_online)
    TextView scTvOnline;
    @BindView(R2.id.background)
    ImageView background;
    @BindView(R2.id.sc_fl_conversation)
    FrameLayout scFlConversation;

    @Autowired
    String targetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListener();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_social_conversation;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        GroupProfile profile = GroupHelper.getInstance().getGroupProfile(targetId);
        if (profile != null) {
            scTvTitle.setText(profile.getName());
            TIMGroupManagerExt.getInstance().getGroupDetailInfo(Arrays.asList(targetId), new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
                    for (TIMGroupDetailInfo info : timGroupDetailInfos) {
                        if (info.getGroupId().equals(targetId) && scTvOnline != null) {
                            scTvOnline.setText(UIUtil.getString(R.string.im_online_number, info.getOnlineMemberNum()));
                        }
                    }
                }
            });
        }

        ConversationFragment conversationFragment = ConversationFragment.newInstance(ConversationType.SOCIAL, targetId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.sc_fl_conversation, conversationFragment, ConversationFragment.class.getName() + "social");
        transaction.commitAllowingStateLoss();
    }

    private void setListener() {
        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ConversationFragment.class.getName() + "social");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
