package com.wang.social.funpoint.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.Tag;
import com.wang.social.funpoint.R;
import com.wang.social.funpoint.R2;
import com.wang.social.funpoint.mvp.entities.Lable;

import butterknife.BindView;


public class RecycleAdapterLable extends BaseAdapter<Tag> {

    private final static int TYPE_SRC_ITEM = R.layout.funpoint_item_lable;
    private final static int TYPE_SRC_MORE = R.layout.funpoint_item_lable_more;

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        if (viewType == TYPE_SRC_ITEM) {
            return new HolderItem(context, parent, viewType);
        } else if (viewType == TYPE_SRC_MORE) {
            return new HolderMore(context, parent, viewType);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderItem) {
            ((BaseViewHolder) holder).setData(valueList.get(position), position, onItemClickListener);
        } else if (holder instanceof HolderMore) {
            ((BaseViewHolder) holder).setData(null, position, onItemClickListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_SRC_MORE;
        } else {
            return TYPE_SRC_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return valueList == null ? 1 : valueList.size() + 1;
    }


    public class HolderItem extends BaseViewHolder<Tag> {
        @BindView(R2.id.text_name)
        TextView text_name;

        public HolderItem(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Tag lable, int position, OnItemClickListener onItemClickListener) {
            text_name.setText(lable.getTagName());
        }

        @Override
        public void onRelease() {
        }
    }

    public class HolderMore extends BaseViewHolder<Tag> {

        public HolderMore(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Tag lable, int position, OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener((view) -> {
                if (onMoreClickListener != null) onMoreClickListener.onMoreClick(view);
            });
        }

        @Override
        public void onRelease() {
        }
    }

    /////////////////////////////////

    private OnMoreClickListener onMoreClickListener;

    public void setOnMoreClickListener(OnMoreClickListener onMoreClickListener) {
        this.onMoreClickListener = onMoreClickListener;
    }

    public interface OnMoreClickListener {
        void onMoreClick(View v);
    }
}
