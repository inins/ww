package com.wang.social.im.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.frame.component.view.SocialToolbar;
import com.tencent.imcore.GroupManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.wang.social.im.R;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.mvp.ui.ConversationFragment;
import com.wang.social.im.mvp.ui.fragments.GameConversationFragment;

import timber.log.Timber;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-11 14:36
 * ============================================
 */
public class GameConversationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_activity_game_conversation);

        GameConversationFragment conversationFragment = GameConversationFragment.newInstance("129");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.conversation, conversationFragment, ConversationFragment.class.getName());
        transaction.commitAllowingStateLoss();

        SocialToolbar toolbar = findViewById(R.id.g_toolbar);
        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.RIGHT_TEXT) {
                    TIMGroupManager.CreateGroupParam param = new TIMGroupManager.CreateGroupParam(IMConstants.IM_GROUP_TYPE_AV_CHAT_ROOM, "摇钱树");
                    param.setGroupId(IMConstants.IM_IDENTITY_PREFIX_GAME + "129");
                    TIMGroupManager.getInstance().createGroup(param, new TIMValueCallBack<String>() {
                        @Override
                        public void onError(int i, String s) {
                            Timber.tag("createGroup").e(s);
                        }

                        @Override
                        public void onSuccess(String s) {
                            Timber.tag("createGroup").i("创建成功：" + s);
                        }
                    });
                }
            }
        });
    }
}
