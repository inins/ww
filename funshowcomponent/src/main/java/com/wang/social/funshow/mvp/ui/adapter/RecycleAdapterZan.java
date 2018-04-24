package com.wang.social.funshow.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.User;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.user.ZanUser;

import butterknife.BindView;


public class RecycleAdapterZan extends BaseAdapter<ZanUser> {

    private final static int TYPE_SRC_ITEM = R.layout.funshow_item_zan;
    private final static int TYPE_SRC_MORE = R.layout.funshow_item_zan_more;

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


    public class HolderItem extends BaseViewHolder<ZanUser> {
        @BindView(R2.id.img_header)
        ImageView img_header;

        public HolderItem(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(ZanUser bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(img_header, bean.getAvatar());
        }

        @Override
        public void onRelease() {
        }
    }

    public class HolderMore extends BaseViewHolder<ZanUser> {

        public HolderMore(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(ZanUser lable, int position, OnItemClickListener onItemClickListener) {
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
