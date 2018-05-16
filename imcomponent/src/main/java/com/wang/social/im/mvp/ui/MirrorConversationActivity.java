package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.CommonHelper;
import com.frame.component.utils.UIUtil;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.Autowired;
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
import butterknife.OnClick;

/**
 * 镜子聊天
 */
public class MirrorConversationActivity extends BasicConversationActivity {

    @BindView(R2.id.mc_tv_online)
    TextView mcTvOnline;
    @BindView(R2.id.mc_iv_team)
    ImageView mcIvTeam;

    @Autowired
    String targetId;

    public static void start(Context context, String targetId) {
        Intent intent = new Intent(context, MirrorConversationActivity.class);
        intent.putExtra("targetId", targetId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setTranslucentForImageView(this, 0, mcIvTeam);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_mirror_conversation;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        GroupProfile profile = GroupHelper.getInstance().getGroupProfile(targetId);
        if (profile != null) {
            TIMGroupManagerExt.getInstance().getGroupDetailInfo(Arrays.asList(targetId), new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
                    for (TIMGroupDetailInfo info : timGroupDetailInfos) {
                        if (info.getGroupId().equals(targetId) && mcTvOnline != null) {
                            mcTvOnline.setText(UIUtil.getString(R.string.im_mirror_online_number, info.getOnlineMemberNum()));
                        }
                    }
                }
            });
        }

        ConversationFragment conversationFragment = ConversationFragment.newInstance(ConversationType.MIRROR, targetId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mc_fl_conversation, conversationFragment, ConversationFragment.class.getName() + "mirror");
        transaction.commitAllowingStateLoss();
    }

    @OnClick(R2.id.mc_iv_team)
    public void onViewClicked() {
        CommonHelper.ImHelper.gotoTeamConversation(this, targetId);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ConversationFragment.class.getName() + "mirror");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
