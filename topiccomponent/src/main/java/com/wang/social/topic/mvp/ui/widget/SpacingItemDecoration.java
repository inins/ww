package com.wang.social.topic.mvp.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.utils.SizeUtils;

public class SpacingItemDecoration extends RecyclerView.ItemDecoration {

    public SpacingItemDecoration() {
        super();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.top = SizeUtils.dp2px(5);
        outRect.bottom = SizeUtils.dp2px(5);
        outRect.left = SizeUtils.dp2px(5);
        outRect.right = SizeUtils.dp2px(5);
    }
}
