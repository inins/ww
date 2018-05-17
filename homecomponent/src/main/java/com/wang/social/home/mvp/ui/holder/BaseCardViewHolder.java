package com.wang.social.home.mvp.ui.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;

public abstract class BaseCardViewHolder<T> extends BaseViewHolder<T> {

    private View imgLike;
    private View imgDislike;

    public BaseCardViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
    }

    public void setLikeAlpha(double value) {
        if (imgLike != null) imgLike.setAlpha((float) value);
    }

    public void setDisLikeAlpha(double value) {
        if (imgDislike != null) imgDislike.setAlpha((float) value);
    }

    public void setLikeView(View view) {
        imgLike = view;
    }

    public void setDisLikeView(View view) {
        imgDislike = view;
    }
}
