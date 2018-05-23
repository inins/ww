package com.wang.social.im.mvp.ui.OfficialChatRobot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BasicActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.RouteNode;
import com.wang.social.im.R;
import com.wang.social.im.R2;

import butterknife.BindView;

@RouteNode(path = "/official_chat_robot", desc = "往往官方号")
public class OfficialChatRobotActivity extends BasicActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, OfficialChatRobotActivity.class);
        context.startActivity(intent);
    }

    // 标题栏
    @BindView(R2.id.toolbar)
    SocialToolbar toolbar;
    // 头像
    @BindView(R2.id.pc_iv_portrait)
    ImageView pcIvPortrait;
    @BindView(R2.id.pc_tv_nickname)
    // 名称
    TextView pcTvNickname;
    @BindView(R2.id.background)
    ImageView background;
    // 聊天内容
    @BindView(R2.id.pc_fl_conversation)
    FrameLayout pcFlConversation;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_private_conversation;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        // 标题栏左侧按钮退出
        toolbar.setOnButtonClickListener(clickType -> {
            if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                finish();
            }
        });

        pcIvPortrait.setVisibility(View.GONE);
        pcTvNickname.setCompoundDrawables(null, null, null, null);
        pcTvNickname.setText("往往官方号");

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.pc_fl_conversation, new OfficialChatRobotFragment())
                .commit();
    }
}
