package com.wang.social.moneytree.mvp.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.wang.social.moneytree.R;

public class MoneyTreeView extends FrameLayout {
    public MoneyTreeView(@NonNull Context context) {
        this(context, null);
    }

    public MoneyTreeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoneyTreeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context)
                .inflate(R.layout.mt_view_money_tree_bg, this, true);
    }
}
