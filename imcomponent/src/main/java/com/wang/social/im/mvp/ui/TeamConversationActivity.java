package com.wang.social.im.mvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.frame.component.path.ImPath;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.Autowired;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.BarUtils;
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
 * <p>
 * Create by ChenJing on 2018-04-23 14:39
 * ============================================
 */
@RouteNode(path = ImPath.TEAM_PATH, desc = "觅聊会话")
public class TeamConversationActivity extends BasicConversationActivity {

    @BindView(R2.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R2.id.tc_tv_title)
    TextView tcTvTitle;
    @BindView(R2.id.tc_tv_online)
    TextView tcTvOnline;
//    @BindView(R2.id.tc_fl_toolbar)
//    FrameLayout tcFlToolbar;

    @Autowired
    String targetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setTranslucentForImageView(this, 0, toolbar);

        setListener();
    }

    private void setListener() {
        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                    case RIGHT_ICON:
                        MirrorConversationActivity.start(TeamConversationActivity.this, targetId);
                        finish();
                        break;
                }
            }
        });
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_team_conversation;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        loadBackground(ConversationType.TEAM, targetId);

        GroupProfile profile = GroupHelper.getInstance().getGroupProfile(targetId);
        if (profile != null) {
            tcTvTitle.setText(profile.getName());
            TIMGroupManagerExt.getInstance().getGroupDetailInfo(Arrays.asList(targetId), new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
                    for (TIMGroupDetailInfo info : timGroupDetailInfos) {
                        if (info.getGroupId().equals(targetId) && tcTvOnline != null) {
                            tcTvOnline.setText(UIUtil.getString(R.string.im_online_total_number, info.getMemberNum(), info.getOnlineMemberNum()));
                        }
                    }
                }
            });
        }

        ConversationFragment conversationFragment = ConversationFragment.newInstance(ConversationType.TEAM, targetId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.tc_fl_conversation, conversationFragment, ConversationFragment.class.getName() + "team");
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ConversationFragment.class.getName() + "team");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
