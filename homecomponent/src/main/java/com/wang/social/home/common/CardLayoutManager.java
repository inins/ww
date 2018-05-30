package com.wang.social.home.common;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.frame.utils.SizeUtils;

/**
 * 自定义卡片布局管理器，从底部开始向上排列
 */

public class CardLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "swipecard";

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int itemCount = getItemCount();
        if (itemCount < 1) {
            return;
        }
        //top-3View的position
        int bottomPosition;
        //边界处理
        if (itemCount < CardConfig.MAX_SHOW_COUNT) {
            bottomPosition = 0;
        } else {
            bottomPosition = itemCount - CardConfig.MAX_SHOW_COUNT;
        }

        //从可见的最底层View开始layout，依次层叠上去
        for (int position = bottomPosition; position < itemCount; position++) {
            //添加并测量视图
            View view = recycler.getViewForPosition(position);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            //固定到屏幕中间
            int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
            layoutDecoratedWithMargins(view, widthSpace / 2, getPaddingTop(), widthSpace / 2 + getDecoratedMeasuredWidth(view), getPaddingTop() + getDecoratedMeasuredHeight(view));

            //计算可见视图总数
            int viewCount = itemCount > CardConfig.MAX_SHOW_COUNT ? CardConfig.MAX_SHOW_COUNT : itemCount;

            //第几层,举例子，count =7， 最后一个TopView（6）是第0层，
            int level = itemCount - position - 1;

            if (level == 0) {
                //第一个
                view.setTranslationY(0);
                view.setScaleY(1);
                view.setScaleX(1);
            } else if (level == viewCount - 1 && viewCount >= CardConfig.MAX_SHOW_COUNT) {
                //最后一个
                //Y方向位移及缩小等同于上一级
                view.setTranslationY(CardConfig.TRANS_Y_GAP * (level - 1));
                view.setScaleY(1 - CardConfig.SCALE_GAP * (level - 1));
                //X方向缩小到当前等级
                view.setScaleX(1 - CardConfig.SCALE_GAP * level);
            } else {
                //中间的
                //Y方向位移及缩小到当前等级
                view.setTranslationY(CardConfig.TRANS_Y_GAP * (level));
                view.setScaleY(1 - CardConfig.SCALE_GAP * (level));
                //X方向缩小到当前等级
                view.setScaleX(1 - CardConfig.SCALE_GAP * level);
            }
        }
    }

    public static class CardConfig {
        //屏幕上最多同时显示几个Item
        public static int MAX_SHOW_COUNT = 4;

        //每一级Scale相差0.08f，translationY相差7dp左右
        public static float SCALE_GAP = 0.08f;

        //每一级向下位移多少dp
        public static int TRANS_Y_GAP = SizeUtils.dp2px(28);
    }

}
