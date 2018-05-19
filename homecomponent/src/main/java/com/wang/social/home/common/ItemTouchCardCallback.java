package com.wang.social.home.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.wang.social.home.common.CardLayoutManager.CardConfig;
import com.wang.social.home.mvp.ui.adapter.RecycleAdapterCardUser;
import com.wang.social.home.mvp.ui.holder.BaseCardViewHolder;

import java.util.List;


/**
 * 自定义卡片布局管理器拖拽手势回调处理器
 */

public class ItemTouchCardCallback extends ItemTouchHelper.SimpleCallback {

    private Context context;
    protected RecyclerView mRv;
    protected List mDatas;
    protected RecyclerView.Adapter mAdapter;

    private static final int MAX_ROTATION = 15;

    public ItemTouchCardCallback(RecyclerView rv, List datas) {
        this(0,
                ItemTouchHelper.DOWN | ItemTouchHelper.UP |
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                , rv, datas);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
//        return super.isItemViewSwipeEnabled();
        return false;
    }

    public ItemTouchCardCallback(int dragDirs, int swipeDirs, RecyclerView rv, List datas) {
        super(dragDirs, swipeDirs);
        mRv = rv;
        mAdapter = rv.getAdapter();
        mDatas = datas;
        context = rv.getContext();
    }

    private float fz = 0.5f;

    //阈值
    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        float x = viewHolder.itemView.getX();
        float y = viewHolder.itemView.getY();
        Rect rect = new Rect();
        viewHolder.itemView.getLocalVisibleRect(rect);
        Log.e("test", "x:" + x + " y:" + y + " rect:" + rect.toString());
        return fz;
    }

    //swipe的逃逸速度，换句话说就算没达到getSwipeThreshold设置的距离，达到了这个逃逸速度item也会被swipe消失掉
    //这里设置为最大值，不允许这种操作
    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return Float.MAX_VALUE;
    }

    //水平方向是否可以被回收掉的距离阈值
    public float getThreshold(RecyclerView.ViewHolder viewHolder) {
        //考虑 探探垂直上下方向滑动，不删除卡片，这里参照源码写死0.5f
        return mRv.getWidth() * 0.5f;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //★实现循环的要点
        Object remove = mDatas.remove(viewHolder.getLayoutPosition());
//        mDatas.add(0, remove);
        mAdapter.notifyDataSetChanged();

        //item复位
        viewHolder.itemView.setRotation(0);
        if (viewHolder instanceof BaseCardViewHolder) {
            ((BaseCardViewHolder) viewHolder).setLikeAlpha(0);
            ((BaseCardViewHolder) viewHolder).setDisLikeAlpha(0);
        }

        if (onSwipedListener != null)
            onSwipedListener.onSwiped(remove, direction);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (Math.abs(dY) > Math.abs(dX)) {
            fz = 2;
        } else {
            fz = 0.5f;
        }
        //先根据滑动的dxdy 算出现在动画的比例系数fraction
        double swipValue = Math.sqrt(dX * dX + dY * dY);
        double fraction = swipValue / getThreshold(viewHolder);
        //边界修正 最大为1
        if (fraction > 1) {
            fraction = 1;
        }
        //对每个ChildView进行缩放 位移
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = recyclerView.getChildAt(i);
            int viewCount = childCount > CardConfig.MAX_SHOW_COUNT ? CardConfig.MAX_SHOW_COUNT : childCount;
            //第几层,举例子，count =7， 最后一个TopView（6）是第0层，
            int level = childCount - i - 1;

            if (level == 0) {
                //第一个
                //不位移，不缩放
                child.setScaleY(1);
                child.setScaleX(1);
                //根据X移动距离进行选择
                double fractionX = dX / getThreshold(viewHolder);
                child.setRotation((float) fractionX * MAX_ROTATION);
                //根据X移动距离进行like和dislike的渐变
                if (viewHolder instanceof BaseCardViewHolder) {
                    if (dX > 0)
                        ((BaseCardViewHolder) viewHolder).setLikeAlpha(Math.abs(fractionX));
                    else
                        ((BaseCardViewHolder) viewHolder).setDisLikeAlpha(Math.abs(fractionX));
                }
            } else if (level == viewCount - 1 && viewCount >= CardConfig.MAX_SHOW_COUNT) {
                //最后一个
                //Y方向位移及缩小等同于上一级
                child.setScaleY(1 - CardConfig.SCALE_GAP * (level - 1));
                child.setTranslationY(CardConfig.TRANS_Y_GAP * (level - 1));
                //X方向缩小到当前等级，并从0-1向上一等级变化
                child.setScaleX((float) (1 - CardConfig.SCALE_GAP * level + fraction * CardConfig.SCALE_GAP));
            } else {
                //中间的
                //Y方向位移及缩小到当前等级，并从0-1向上一等级变化
                child.setScaleY((float) (1 - CardConfig.SCALE_GAP * level + fraction * CardConfig.SCALE_GAP));
                child.setTranslationY((float) (CardConfig.TRANS_Y_GAP * level - fraction * CardConfig.TRANS_Y_GAP));
                //X方向缩小到当前等级，并从0-1向上一等级变化
                child.setScaleX((float) (1 - CardConfig.SCALE_GAP * level + fraction * CardConfig.SCALE_GAP));
            }
        }
    }

    private OnSwipedListener onSwipedListener;

    public void setOnSwipedListener(OnSwipedListener onSwipedListener) {
        this.onSwipedListener = onSwipedListener;
    }

    public interface OnSwipedListener<T> {
        void onSwiped(T t, int direction);
    }
}
