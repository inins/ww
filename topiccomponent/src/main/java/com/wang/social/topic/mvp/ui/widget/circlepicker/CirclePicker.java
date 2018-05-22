package com.wang.social.topic.mvp.ui.widget.circlepicker;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CirclePicker extends RecyclerView {
    public final static String TAG = CirclePicker.class.getSimpleName().toString();

    public interface SelectListener {
        void onItemSelected(int position, CirclePickerItem item);
    }

    private LinearLayoutManager mLayoutManager;
    private SelectListener mSelectListener;
    private CirclePickerAdapter mAdapter;

    public CirclePicker(Context context) {
        this(context, null);
    }

    public CirclePicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePicker(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    public void setSelectListener(SelectListener selectListener) {
        mSelectListener = selectListener;
    }

    private void init() {
        mLayoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        setLayoutManager(mLayoutManager);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                switch (newState) {
                    case SCROLL_STATE_IDLE:
                        int firstPos = mLayoutManager.findFirstVisibleItemPosition();
                        int lastPos = mLayoutManager.findLastVisibleItemPosition();
                        View child = recyclerView.getChildAt(0);
//                        Log.i(TAG, String.format("静止 %d %d", firstPos, lastPos));
//                        Log.i(TAG, String.format("%d %d %d %d", child.getLeft(), child.getTop(), child.getRight(), child.getBottom()));
//                        Log.i(TAG, String.format("%f %f", child.getX(), child.getY()));
                        int[] outLocation = new int[2];
                        child.getLocationOnScreen(outLocation);
//                        Log.i(TAG, Arrays.toString(outLocation));

                        int offset = Math.abs(child.getLeft());
                        int halfWidth = child.getWidth() / 2;

//                        Log.i(TAG, String.format("offset=%d halfWidth=%d", offset, halfWidth));

                        if (offset != 0) {
                            if (offset > halfWidth) {
                                recyclerView.smoothScrollBy(child.getWidth() - offset, 0);
                            } else if (offset < halfWidth) {
                                recyclerView.smoothScrollBy(child.getLeft(), 0);
                            }
                        } else {
//                            Log.i(TAG, "停止");
                            if (null != mSelectListener && null != mAdapter) {
                                int pos = mAdapter.getMiddleItemPos(firstPos);
                                mSelectListener.onItemSelected(pos, mAdapter.getItem(pos));
                            }
                        }
                        break;
                    case SCROLL_STATE_DRAGGING:
//                        Log.i(TAG, "拖动");
                        break;
                    case SCROLL_STATE_SETTLING:
//                        Log.i(TAG, "滑动");
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public void setAdapter(CirclePickerAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = adapter;
    }

    public void setCurrentItem(int position) {
        if (null != mAdapter) {
            mAdapter.setCurrentItem(position);
        }
    }

    public int getCurrentItem() {
        if (null != mAdapter) {
            return mAdapter.getCurrentItem();
        }

        return 0;
    }
}
