package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.StrUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.frame.component.entities.photo.Photo;

import butterknife.BindView;

public class RecycleAdapterMePhoto extends BaseAdapter<Photo> {

    private final static int TYPE_SRC_ITEM = R.layout.personal_item_mephoto;
    private final static int TYPE_SRC_ADD = R.layout.personal_item_mephoto_add;

    private ImageLoader mImageLoader;

    public RecycleAdapterMePhoto(ImageLoader mImageLoader) {
        this.mImageLoader = mImageLoader;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        if (viewType == TYPE_SRC_ITEM) {
            return new HolderItem(context, parent, viewType);
        } else if (viewType == TYPE_SRC_ADD) {
            return new HolderAdd(context, parent, viewType);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderItem) {
            ((BaseViewHolder) holder).setData(valueList.get(position), position, onItemClickListener);
        } else if (holder instanceof HolderAdd) {
            ((BaseViewHolder) holder).setData(null, position, onItemClickListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_SRC_ADD;
        } else {
            return TYPE_SRC_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return valueList == null ? 1 : valueList.size() + 1;
    }

    public class HolderItem extends BaseViewHolder<Photo> {
        @BindView(R2.id.img_photo)
        ImageView imgPhoto;
        @BindView(R2.id.btn_del)
        ImageView btnDel;
        @BindView(R2.id.btn_modify)
        TextView btnModify;

        public HolderItem(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Photo bean, int position, OnItemClickListener onItemClickListener) {
            mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                    .imageView(imgPhoto)
                    .url(bean.getPhotoUrl())
                    .build());
            itemView.setOnClickListener(view -> {
                if (onPhotoClickListener != null)
                    onPhotoClickListener.onItemClick(view, bean, position);
            });
            btnDel.setOnClickListener(view -> {
                if (onPhotoClickListener != null) onPhotoClickListener.onDel(view, bean, position);
            });
            btnModify.setOnClickListener(view -> {
                if (onPhotoClickListener != null)
                    onPhotoClickListener.onModify(view, bean, position);
            });
        }

        @Override
        public void onRelease() {
        }
    }

    public class HolderAdd extends BaseViewHolder<Photo> {
        public HolderAdd(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Photo itemValue, int position, OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(view -> {
                if (onPhotoClickListener != null) onPhotoClickListener.onAdd(view);
            });
            if (valueList != null && valueList.size() >= 3) {
                itemView.setVisibility(View.GONE);
            } else {
                itemView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onRelease() {
        }
    }

    public void removeItemById(int id) {
        int position = getPositionById(id);
        if (position != -1) removeItem(position);
    }

    public void modifyItemById(int id) {
        int position = getPositionById(id);
        if (position != -1) notifyItemChanged(position);
    }

    public int getPositionById(int id) {
        if (getData() == null) return -1;
        for (int i = 0; i < getData().size(); i++) {
            Photo photo = getData().get(i);
            if (photo.getUserPhotoId() == id) {
                return i;
            }
        }
        return -1;
    }
    ////////////////////////////////

    public int getResultsCount() {
        if (StrUtil.isEmpty(getData())) return 0;
        return getData().size();
    }

    private OnPhotoClickListener onPhotoClickListener;

    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }

    public interface OnPhotoClickListener {
        void onAdd(View v);

        void onDel(View v, Photo photo, int position);

        void onModify(View v, Photo photo, int position);

        void onItemClick(View v, Photo photo, int position);
    }
}
