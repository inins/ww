package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.path.ImPath;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.router.facade.annotation.Autowired;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.ScreenUtils;
import com.frame.utils.SizeUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerActivityComponent;
import com.wang.social.im.enums.ConversationType;
import com.wang.social.im.helper.FriendShipHelper;
import com.wang.social.im.mvp.model.entities.FriendProfile;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * ============================================
 * 个人会话界面
 * <p>
 * Create by ChenJing on 2018-04-21 18:05
 * ============================================
 */
@RouteNode(path = ImPath.PRIVATE_PATH, desc = "个人聊天")
public class PrivateConversationActivity extends BasicConversationActivity {

    @BindView(R2.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R2.id.pc_iv_portrait)
    ImageView pcIvPortrait;
    @BindView(R2.id.pc_tv_nickname)
    TextView pcTvNickname;
    @BindView(R2.id.background)
    ImageView background;
    @BindView(R2.id.pc_fl_conversation)
    FrameLayout pcFlConversation;

    @Autowired
    String targetId;

    @Inject
    ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListener();
    }

    private void setListener() {
        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType){
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                }
            }
        });
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerActivityComponent
                .builder()
                .appComponent(appComponent)
                .build().inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_private_conversation;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        initBackground(ConversationType.PRIVATE, targetId);

        ConversationFragment conversationFragment = ConversationFragment.newInstance(ConversationType.PRIVATE, targetId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.pc_fl_conversation, conversationFragment, ConversationFragment.class.getName() + "private");
        transaction.commitAllowingStateLoss();

        FriendProfile profile = FriendShipHelper.getInstance().getFriendProfile(targetId);
        if (profile != null) {
            mImageLoader.loadImage(this, ImageConfigImpl
                    .builder()
                    .placeholder(R.drawable.common_default_circle_placeholder)
                    .errorPic(R.drawable.common_default_circle_placeholder)
                    .isCircle(true)
                    .imageView(pcIvPortrait)
                    .url(profile.getPortrait())
                    .build());

            pcTvNickname.setText(profile.getName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        targetId = null;
        mImageLoader = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ConversationFragment.class.getName() + "private");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}