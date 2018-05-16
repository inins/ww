package com.wang.social.im.mvp.ui;

import android.widget.FrameLayout;
import android.widget.ImageView;

import com.frame.component.ui.base.BasicAppActivity;
import com.frame.utils.ScreenUtils;
import com.frame.utils.SizeUtils;
import com.wang.social.im.R;
import com.frame.component.enums.ConversationType;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-23 13:22
 * ============================================
 */
public abstract class BasicConversationActivity extends BasicAppActivity {

    /**
     * 初始化背景
     */
    protected void initBackground(ConversationType conversationType, String targetId) {
        ImageView background = findViewById(R.id.background);
        if (background != null) {
            //初始化背景图片高度
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) background.getLayoutParams();
            lp.height = ScreenUtils.getScreenHeight() - SizeUtils.dp2px(40);

            switch (conversationType) {
                case PRIVATE:
                    background.setImageResource(R.drawable.im_bg_conversaton_default);
                    break;
                case SOCIAL:
                    background.setImageResource(R.drawable.im_bg_conversaton_default);
                    break;
                case TEAM:
                    background.setImageResource(R.drawable.im_bg_conversaton_team_default);
                    break;
            }
        }
    }
}