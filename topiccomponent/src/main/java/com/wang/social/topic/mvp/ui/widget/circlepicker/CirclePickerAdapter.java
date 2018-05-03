package com.wang.social.topic.mvp.ui.widget.circlepicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.social.topic.R;

import java.util.List;

public class CirclePickerAdapter extends RecyclerView.Adapter<CirclePickerAdapter.ViewHolder> {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private List<CirclePickerItem> mList;
    // 屏幕宽度，需要改为父控件宽度
    private int mScreenWidth;
    // 显示行数
    private int mDisplayCount = 5;
    //
    private float mScale;

    public CirclePickerAdapter(RecyclerView recyclerView, List<CirclePickerItem> list) {
        mContext = recyclerView.getContext().getApplicationContext();
        mRecyclerView = recyclerView;
        mScale = mContext.getResources().getDisplayMetrics().density;
        // 获取父控件宽度
        recyclerView.getWidth();
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.topic_view_circle_picker_item, parent, false);

        view.setLayoutParams(
                new LinearLayout.LayoutParams(
                        mScreenWidth / mDisplayCount,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        return new ViewHolder(view);
    }

    public CirclePickerItem getItem(int position) {
        if (null == mList) return null;
        if (position < 0 || position >= mList.size()) {
            return null;
        }

        return mList.get(position);
    }

    public void setCurrentItem(int position) {
        int pos = Integer.MAX_VALUE / 2;
        pos = pos - (pos % mList.size());
        mRecyclerView.scrollToPosition(pos + position - getHalfColumn());
    }

    public int getCurrentItem() {
        int firstPos = 0;
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            firstPos = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }

        return getMiddleItemPos(firstPos);
    }

    private int getHalfColumn() {
        return mDisplayCount % 2 == 1 ? (mDisplayCount - 1) / 2 : mDisplayCount / 2;
    }

    /**
     * 获取位于recyclerview列表中间的位置
     * @param firstVisiblePosition 第一个可见元素位置
     */
    public int getMiddlePos(int firstVisiblePosition) {
        int halfColumn = getHalfColumn();
        return firstVisiblePosition + halfColumn;
    }

    /**
     * 获取当前位置在实际元素列表中的位置
     * @param firstVisiblePosition 第一个可见元素位置
     */
    public int getMiddleItemPos(int firstVisiblePosition) {
        return getMiddlePos(firstVisiblePosition) % mList.size();
    }

    public int getDisplayCount() {
        return mDisplayCount;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (null == mList) return;
        if (position < 0) return;
        CirclePickerItem item = mList.get(position % mList.size());

        if (null != item.getDrawable()) {
            holder.mIV.setVisibility(View.VISIBLE);
            holder.mIV.setImageDrawable(item.getDrawable());
        } else {
            holder.mIV.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(item.getText())) {
            holder.mTV.setVisibility(View.VISIBLE);
            holder.mTV.setText(item.getText());
            if (item.getTextSize() > 0) {
                holder.mTV.setTextSize((int)(dp2px(item.getTextSize()) / mScale));
            }
        } else {
            holder.mTV.setVisibility(View.GONE);
        }
    }


    public int dp2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public int getItemCount() {
//        return null == mList ? 0 : mList.length;
        return Integer.MAX_VALUE;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIV;
        TextView mTV;
        public ViewHolder(View itemView) {
            super(itemView);

            mIV = itemView.findViewById(R.id.image_view);
            mTV = itemView.findViewById(R.id.text_view);
        }
    }
}
