package com.frame.component.ui.acticity.PersonalCard.ui.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by King on 2018/3/28.
 */

public class ThumbnailDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private int mSpacing;
    boolean mHasEdge = true;

    public ThumbnailDecoration(int spacing, @ColorInt int color, boolean hasEdge) {
        mSpacing = spacing;

        if (color != Color.TRANSPARENT) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.FILL);
        }
        
        this.mHasEdge = hasEdge;
    }


    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {
        // 列数
        int spanCount = getSpanCount(parent);
        // 当前item位置
        int position = parent.getChildAdapterPosition(view);
        // 当前item的行数
        int column = position % spanCount;

        if (mHasEdge) {
            outRect.left = mSpacing - column * mSpacing / spanCount;
            outRect.right = (column + 1) * mSpacing / spanCount;

            if (position < spanCount) {
                outRect.top = mSpacing;
            }
            outRect.bottom = mSpacing;
        } else {
            outRect.left = column * mSpacing / spanCount;
            outRect.right = mSpacing - (column + 1) * mSpacing / spanCount;
            if (position >= spanCount) {
                outRect.top = mSpacing;
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        draw(c, parent);
    }

    private void draw(Canvas canvas, RecyclerView parent) {
        if (null == canvas) return;
        if (null == mPaint) return;

        int childSize = parent.getChildCount();

        for (int i = 0; i < childSize; i++) {
            View child = parent.getChildAt(i);

            int top = child.getTop();
            int bottom = child.getBottom();
            int left = child.getLeft();
            int right = child.getRight();

            // 左边
            canvas.drawRect(left - mSpacing,
                    top - mSpacing,
                    left + mSpacing,
                    bottom + mSpacing,
                    mPaint);

            // 上边
            canvas.drawRect(left,
                    top - mSpacing,
                    right,
                    top + mSpacing,
                    mPaint);

            // 右边
            canvas.drawRect(right,
                    top - mSpacing,
                    right + mSpacing,
                    bottom + mSpacing,
                    mPaint);

            // 下边
            canvas.drawRect(left,
                    bottom,
                    right,
                    bottom + mSpacing,
                    mPaint);
        }
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = 1;

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }
}
