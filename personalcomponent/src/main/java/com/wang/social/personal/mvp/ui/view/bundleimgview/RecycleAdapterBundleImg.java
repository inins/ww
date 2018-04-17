package com.wang.social.personal.mvp.ui.view.bundleimgview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wang.social.personal.R;
import com.wang.social.personal.common.DragItemTouchCallback;

import java.util.Collections;
import java.util.List;


public class RecycleAdapterBundleImg extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DragItemTouchCallback.ItemTouchAdapter {

    private List<BundleImgEntity> results;
    private boolean enable = true;
    //最大图片数量，>0时才有效
    private int maxcount;
    private BundleImgView.OnBundleLoadImgListener onBundleLoadImgListener;

    public static final int TYPE_ITEM = 0xff01;
    public static final int TYPE_ADD = 0xff02;

    public void setEditble(boolean enable) {
        this.enable = enable;
    }

    public List<BundleImgEntity> getResults() {
        return results;
    }

    public RecycleAdapterBundleImg(List<BundleImgEntity> results) {
        this.results = results;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ADD:
                return new HolderAdd(LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_bundle_item_recycle_add, parent, false));
            case TYPE_ITEM:
                return new HolderItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_bundle_item_recycle, parent, false));
            default:
                Log.d("error", "viewholder is null");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(holder, position);
            }
        });

        if (holder instanceof HolderAdd) {
            bindTypeAdd((HolderAdd) holder, position);
        } else if (holder instanceof HolderItem) {
            bindTypeItem((HolderItem) holder, position);
        }
    }

    private void bindTypeAdd(HolderAdd holder, int position) {
        holder.itemView.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundleClickListener != null) {
                    bundleClickListener.onPhotoAddClick(v);
                }
            }
        });
        if (results != null && results.size() >= maxcount) {
            holder.itemView.setVisibility(View.GONE);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
        }
    }

    private void bindTypeItem(final HolderItem holder, int position) {
        final BundleImgEntity bundle = results.get(position);
        holder.img_bundle_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundleClickListener != null)
                    bundleClickListener.onPhotoShowClick(bundle.getPath());
            }
        });
        if (onBundleLoadImgListener != null) {
            onBundleLoadImgListener.onloadImg(holder.img_bundle_show, bundle.getPath(), 0);
        }
        holder.img_bundle_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.remove(holder.getLayoutPosition());
                notifyItemRemoved(holder.getLayoutPosition());
                notifyItemChanged(getItemCount() - 1);
                if (bundleClickListener != null) {
                    bundleClickListener.onPhotoDelClick(v);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (enable) {
            return results.size() + 1;
        } else {
            return results.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == results.size()) {
            return TYPE_ADD;
        } else {
            return TYPE_ITEM;
        }
    }

    public class HolderItem extends RecyclerView.ViewHolder {
        private ImageView img_bundle_show;
        private ImageView img_bundle_delete;

        public HolderItem(View itemView) {
            super(itemView);
            img_bundle_show = (ImageView) itemView.findViewById(R.id.img_bundle_show);
            img_bundle_delete = (ImageView) itemView.findViewById(R.id.img_bundle_delete);
            if (enable) {
                img_bundle_delete.setVisibility(View.VISIBLE);
            } else {
                img_bundle_delete.setVisibility(View.INVISIBLE);
            }
        }
    }

    public class HolderAdd extends RecyclerView.ViewHolder {
        public HolderAdd(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition == getItemCount() - 1 || toPosition == getItemCount() - 1) {
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(results, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(results, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        results.remove(position);
        notifyItemRemoved(position);
    }

    private BundleImgView.OnBundleClickListener bundleClickListener;

    public void setOnBundleLoadImgListener(BundleImgView.OnBundleLoadImgListener onBundleLoadImgListener) {
        this.onBundleLoadImgListener = onBundleLoadImgListener;
    }

    public void setBundleClickListener(BundleImgView.OnBundleClickListener bundleClickListener) {
        this.bundleClickListener = bundleClickListener;
    }

    private OnBunldItemClickListener listener;

    public interface OnBunldItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int position);
    }

    public void setOnItemClickListener(OnBunldItemClickListener listener) {
        this.listener = listener;
    }

    public int getMaxcount() {
        return maxcount;
    }

    public void setMaxcount(int maxcount) {
        this.maxcount = maxcount;
    }
}
