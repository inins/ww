package com.frame.component.common;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HDHeadItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public HDHeadItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) != 0) {
            outRect.left = space;
        }
    }
}
