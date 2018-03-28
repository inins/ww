package com.frame.component.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.service.R;

/**
 * Created by Bo on 2018-03-27.
 */

public class SocialToolbar extends Toolbar {

    private ImageView ivLeft, ivRight;
    private TextView tvRight;

    private Drawable mLeftIcon, mRightIcon;
    private String mRightText;
    private int mRightTextColor;

    private OnButtonClickListener buttonClickListener;

    public SocialToolbar(Context context) {
        this(context, null);
    }

    public SocialToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public SocialToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int defColor = ContextCompat.getColor(context, R.color.common_colorAccent);

        TintTypedArray typedArray = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.SocialToolbar, defStyleAttr, 0);
        mLeftIcon = typedArray.getDrawable(R.styleable.SocialToolbar_toolbar_left_icon);
        mRightIcon = typedArray.getDrawable(R.styleable.SocialToolbar_toolbar_right_icon);
        mRightText = typedArray.getString(R.styleable.SocialToolbar_toolbar_right_text);
        mRightTextColor = typedArray.getColor(R.styleable.SocialToolbar_toolbar_right_text_color, defColor);
        typedArray.recycle();

        inflate(context, R.layout.comp_toolbar, this);

        initView();
    }

    private void initView() {
        ivLeft = findViewById(R.id.toolbar_iv_left);
        ivRight = findViewById(R.id.toolbar_iv_right);
        tvRight = findViewById(R.id.toolbar_tv_right);

        if (mLeftIcon != null) {
            ivLeft.setVisibility(VISIBLE);
            ivLeft.setImageDrawable(mLeftIcon);
        }
        if (mRightIcon != null){
            ivRight.setVisibility(VISIBLE);
            ivRight.setImageDrawable(mRightIcon);
        }else if (!TextUtils.isEmpty(mRightText)){
            tvRight.setVisibility(VISIBLE);
            tvRight.setText(mRightText);
            tvRight.setTextColor(mRightTextColor);
        }

        ivLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonClickListener != null){
                    buttonClickListener.onButtonClick(ClickType.LEFT_ICON);
                }
            }
        });

        ivRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonClickListener != null){
                    buttonClickListener.onButtonClick(ClickType.RIGHT_ICON);
                }
            }
        });

        tvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonClickListener != null){
                    buttonClickListener.onButtonClick(ClickType.RIGHT_TEXT);
                }
            }
        });
    }

    public void setLeftIcon(@DrawableRes int resId){
        ivLeft.setVisibility(VISIBLE);
        ivLeft.setImageResource(resId);
    }

    public void setRightIcon(@DrawableRes int resId){
        ivRight.setVisibility(VISIBLE);
        tvRight.setVisibility(GONE);
        ivRight.setImageResource(resId);
    }

    public void setRightText(@StringRes int resId){
        tvRight.setVisibility(VISIBLE);
        ivRight.setVisibility(GONE);
        tvRight.setText(resId);
        tvRight.setTextColor(mRightTextColor);
    }

    public void setRightText(SpannableString span){
        tvRight.setVisibility(VISIBLE);
        ivRight.setVisibility(GONE);
        tvRight.setText(span);
        tvRight.setTextColor(mRightTextColor);
    }

    public void setRightText(String str){
        tvRight.setVisibility(VISIBLE);
        ivRight.setVisibility(GONE);
        tvRight.setText(str);
        tvRight.setTextColor(mRightTextColor);
    }

    public void setOnButtonClickListener(OnButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

    public enum ClickType{
        LEFT_ICON,
        RIGHT_ICON,
        RIGHT_TEXT
    }

    public interface OnButtonClickListener{

        void onButtonClick(ClickType clickType);
    }
}
