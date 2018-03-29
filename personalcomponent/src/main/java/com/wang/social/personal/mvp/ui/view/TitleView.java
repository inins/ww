package com.wang.social.personal.mvp.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wang.social.personal.R;

/**
 * Created by Administrator on 2018/3/29.
 * 个人中心的页面顶部标题样式都一样，使用titleView进行统一管理，方便修改和拓展
 */

public class TitleView extends FrameLayout {

    private TextView text_title;
    private TextView text_note;

    private String title;
    private String note;

    public TitleView(@NonNull Context context) {
        this(context, null);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.personal_TitleView, 0, 0);
            title = ta.getString(R.styleable.personal_TitleView_personal_textTitle);
            note = ta.getString(R.styleable.personal_TitleView_personal_textNote);
            ta.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        LayoutInflater.from(getContext()).inflate(R.layout.personal_lay_titleview, this, true);
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        text_title = findViewById(R.id.text_title);
        text_note = findViewById(R.id.text_note);

        text_title.setText(title);
        text_note.setText(note);
        text_title.setVisibility(TextUtils.isEmpty(title) ? GONE : VISIBLE);
        text_note.setVisibility(TextUtils.isEmpty(note) ? GONE : VISIBLE);
    }
}
