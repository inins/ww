package com.wang.social.topic.mvp.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wang.social.topic.R;

public class ReleaseTopicBottomBar extends LinearLayout {

    public interface ClickListener {
        void onTemplateClick();
        void onMusicClick();
        void onVoiceClick();
        void onFontClick();
        void onKeyBoardClick();
        void onImageClick();
        void onChargeClick();
    }

    private static final int[] ICON_RES = {
            R.drawable.topic_ic_template,
            R.drawable.topic_ic_music,
            R.drawable.topic_ic_voice,
            R.drawable.topic_ic_font,
            R.drawable.topic_ic_image,
            R.drawable.topic_ic_charge
    };

    private ClickListener mClickListener;
    private boolean mFontMode = true;
    private ImageView mFontIV;

    public ReleaseTopicBottomBar(Context context) {
        this(context, null);
    }

    public ReleaseTopicBottomBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReleaseTopicBottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    private void init() {
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        params.gravity = Gravity.CENTER;

        for (int i = 0; i < ICON_RES.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(ICON_RES[i]);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER);

            if (i == 3) {
                mFontIV = imageView;
            }

            imageView.setTag(i);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mClickListener && v.getTag() instanceof Integer) {
                        switch ((int) v.getTag()) {
                            case 0:
                                mClickListener.onTemplateClick();
                                break;
                            case 1:
                                mClickListener.onMusicClick();
                                break;
                            case 2:
                                mClickListener.onVoiceClick();
                                break;
                            case 3:
                                resetFontIcon();
                                break;
                            case 4:
                                mClickListener.onImageClick();
                                break;
                            case 5:
                                mClickListener.onChargeClick();
                                break;
                        }
                    }
                }
            });

            addView(imageView);
        }
    }

    private void resetFontIcon() {
        if (null == mFontIV) return;

        if (mFontMode) {
            mClickListener.onFontClick();
            setToFontMode();
        } else {
            mClickListener.onKeyBoardClick();
            setToSoftInputMode();
        }
    }

    public void setToFontMode() {
        mFontIV.setImageResource(R.drawable.topic_ic_keyboard);
        mFontMode = false;
    }

    public void setToSoftInputMode() {
        mFontIV.setImageResource(R.drawable.topic_ic_font);
        mFontMode = true;
    }
}
