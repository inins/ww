package com.wang.social.im.mvp.ui.fragments;

import android.widget.FrameLayout;
import android.widget.ImageView;

import com.frame.base.BasicFragment;
import com.frame.component.enums.ConversationType;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.ScreenUtils;
import com.frame.utils.SizeUtils;
import com.wang.social.im.R;
import com.wang.social.im.helper.ImHelper;

import java.io.File;
import java.util.UUID;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-15 14:13
 * ============================================
 */
public abstract class BaseConversationFragment extends BasicFragment {

    /**
     * 初始化背景
     */
    protected void loadBackground(ConversationType conversationType, String targetId, ImageView background, ImageLoader imageLoader) {
        if (background != null) {
            //初始化背景图片高度
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) background.getLayoutParams();
            lp.height = ScreenUtils.getScreenHeight() - SizeUtils.dp2px(40);

            int defaultBackground;
            switch (conversationType) {
                case TEAM:
                    defaultBackground = R.drawable.im_bg_conversaton_team_default;
                    break;
                default:
                    defaultBackground = R.drawable.im_bg_conversaton_default;
                    break;
            }

            String backgroundPath = ImHelper.getBackgroundFileName(conversationType, ImHelper.wangId2ImId(targetId, conversationType));
            File file = new File(ImHelper.getBackgroundCachePath(), backgroundPath);
            if (file.exists()) {
                imageLoader.loadImage(getActivity(), ImageConfigImpl
                        .builder()
                        .placeholder(defaultBackground)
                        .errorPic(defaultBackground)
                        .signature(UUID.randomUUID().toString())
                        .url(file.getPath())
                        .imageView(background)
                        .build());
            } else {
                background.setImageResource(defaultBackground);
            }
        }
    }
}
