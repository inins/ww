package com.frame.component.view.bundleimgview;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.frame.component.common.DragItemTouchCallback;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.service.R;
import com.frame.component.view.SquareFramelayout;

import java.util.Collections;
import java.util.List;


public class RecycleAdapterBundleImg extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DragItemTouchCallback.ItemTouchAdapter {

    private List<BundleImgEntity> results;
    private BundleImgView.OnBundleLoadImgListener onBundleLoadImgListener;

    private boolean enable = true;
    //最大图片数量，>0时才有效
    private int maxcount;
    //图片圆角
    private int corner;
    //图片宽高比例
    private float wihi;
    private int src_addbtn;

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
                return new HolderAdd(LayoutInflater.from(parent.getContext()).inflate(R.layout.bundle_item_recycle_add, parent, false));
            case TYPE_ITEM:
                return new HolderItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.bundle_item_recycle, parent, false));
            default:
                Log.d("error", "viewholder is null");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener((v) -> {
            if (listener != null) listener.onItemClick(holder, position);
        });

        if (holder instanceof HolderAdd) {
            bindTypeAdd((HolderAdd) holder, position);
        } else if (holder instanceof HolderItem) {
            bindTypeItem((HolderItem) holder, position);
        }
    }

    private void bindTypeAdd(HolderAdd holder, int position) {
        holder.card_bundle.setRadius(corner);
        holder.square_framelayout.setWihi(wihi);
        //如果超过最多数量，不在显示最后一个加
        if (results != null && results.size() >= maxcount && maxcount != 0) {
            holder.itemView.setVisibility(View.GONE);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener((v) -> {
            if (bundleClickListener != null) {
                bundleClickListener.onPhotoAddClick(v);
            }
        });
    }

    private void bindTypeItem(final HolderItem holder, int position) {
        final BundleImgEntity bundle = results.get(position);

        holder.card_bundle.setRadius(corner);
        holder.square_framelayout.setWihi(wihi);
        holder.img_bundle_play.setVisibility(bundle.isVideo() ? View.VISIBLE : View.GONE);
        if (onBundleLoadImgListener != null) {
            onBundleLoadImgListener.onloadImg(holder.img_bundle_show, bundle.getPath(), 0);
        } else {
            if (!holder.itemView.isInEditMode()) {
                ImageLoaderHelper.loadImg(holder.img_bundle_show, bundle.getPath());
            }
        }
        holder.img_bundle_show.setOnClickListener((v) -> {
            if (bundleClickListener != null)
                bundleClickListener.onPhotoShowClick(bundle.getPath(), position);
        });
        holder.img_bundle_delete.setOnClickListener((v) -> {
            results.remove(holder.getLayoutPosition());
            notifyItemRemoved(holder.getLayoutPosition());
            notifyItemChanged(getItemCount() - 1);
            if (bundleClickListener != null) {
                bundleClickListener.onPhotoDelClick(v, position);
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
        CardView card_bundle;
        ImageView img_bundle_show;
        ImageView img_bundle_play;
        ImageView img_bundle_delete;
        SquareFramelayout square_framelayout;

        public HolderItem(View itemView) {
            super(itemView);
            card_bundle = itemView.findViewById(R.id.card_bundle);
            square_framelayout = itemView.findViewById(R.id.square_framelayout);
            img_bundle_show = itemView.findViewById(R.id.img_bundle_show);
            img_bundle_play = itemView.findViewById(R.id.img_bundle_play);
            img_bundle_delete = itemView.findViewById(R.id.img_bundle_delete);
            if (enable) {
                img_bundle_delete.setVisibility(View.VISIBLE);
            } else {
                img_bundle_delete.setVisibility(View.INVISIBLE);
            }
        }
    }

    public class HolderAdd extends RecyclerView.ViewHolder {
        CardView card_bundle;
        SquareFramelayout square_framelayout;

        public HolderAdd(View itemView) {
            super(itemView);
            card_bundle = itemView.findViewById(R.id.card_bundle);
            square_framelayout = itemView.findViewById(R.id.square_framelayout);
            //如果有自定义添加按钮的布局，则加载自定义布局
            if (src_addbtn != 0) {
                square_framelayout.removeAllViews();
                LayoutInflater.from(itemView.getContext()).inflate(src_addbtn, square_framelayout, true);
            }
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

    ///////////////////////////////////////////////////

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

    /////////////////////////////////////////////////

    public int getMaxcount() {
        return maxcount;
    }

    public void setMaxcount(int maxcount) {
        this.maxcount = maxcount;
    }

    public int getCorner() {
        return corner;
    }

    public void setCorner(int corner) {
        this.corner = corner;
    }

    public float getWihi() {
        return wihi;
    }

    public void setWihi(float wihi) {
        this.wihi = wihi;
    }

    public int getSrcAddbtn() {
        return src_addbtn;
    }

    public void setSrcAddbtn(int src_addbtn) {
        this.src_addbtn = src_addbtn;
    }
}
