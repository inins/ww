package com.wang.social.login202.mvp.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wang.social.login202.R;

public class Login202TitleView extends FrameLayout {

    private View mRootView;
    private TextView mTitleTV;
    private TextView mHintTV;

    public Login202TitleView(@NonNull Context context) {
        this(context, null);
    }

    public Login202TitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Login202TitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        mRootView = LayoutInflater.from(context)
                .inflate(R.layout.login202_widget_title_view, this, true);

        mTitleTV = mRootView.findViewById(R.id.title_text_view);
        mHintTV = mRootView.findViewById(R.id.hint_text_view);
    }

    public void setTitle(String title) {
        if (null != mTitleTV) {
            mTitleTV.setText(title);
        }
    }

    public void setHint(String hint) {
        if (null != mHintTV) {
            mHintTV.setText(hint);
        }
    }
}
