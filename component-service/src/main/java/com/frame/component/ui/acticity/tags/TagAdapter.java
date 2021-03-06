package com.frame.component.ui.acticity.tags;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.service.R;

import java.lang.ref.WeakReference;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
    /**
     * 数据提供
     */
    public interface DataProvider {
        int getItemCount();
        Tag getItem(int position);
        boolean isDeleteMode();
        boolean isSelected(Tag tag);
    }

    public interface TagClickListener {
        void onTagClick(Tag tag);
        void onDelete(Tag tag);
    }

    Context context;
    WeakReference<DataProvider> dataProvider;
    WeakReference<TagClickListener> clickListener;

    public TagAdapter(Context context, DataProvider dataProvider, TagClickListener clickListener) {
//        this.context = new WeakReference<>(context);
        this.context = context.getApplicationContext();
        this.dataProvider = new WeakReference<>(dataProvider);
        this.clickListener = new WeakReference<>(clickListener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.tags_view_tag, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == dataProvider) return;
        Tag tag = dataProvider.get().getItem(position);
        if (null == tag) return;

        // 设置选中和未选中
        if (dataProvider.get().isSelected(tag)) {
            holder.nameTV.setBackground(
                    context.getResources().
                            getDrawable(R.drawable.tags_shape_rect_corner_solid_blue_deep));
            holder.nameTV.setTextColor(Color.WHITE);
        } else {
            holder.nameTV.setBackground(
                    context.getResources().
                            getDrawable(R.drawable.tags_shape_rect_corner_stroke_blue_deep));
            holder.nameTV.setTextColor(
                    context.getResources().getColor(R.color.common_blue_deep));
        }
        holder.nameTV.setText(tag.getTagName());

        if (dataProvider.get().isDeleteMode()) {
            // 删除
            holder.deleteIV.setTag(tag);
            holder.deleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() instanceof Tag) {

                        if (null != clickListener) {
                            clickListener.get().onDelete((Tag) v.getTag());
                        }
                    }
                }
            });
        } else {
            // 选择模式下删除按钮不可见
            holder.deleteIV.setVisibility(View.GONE);
            // 点击
            holder.nameTV.setTag(tag);
            holder.nameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() instanceof Tag) {

                        if (null != clickListener) {
                            clickListener.get().onTagClick((Tag) v.getTag());
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataProvider == null ? 0 : dataProvider.get().getItemCount();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV;
        ImageView deleteIV;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.name_text_view);
            deleteIV = itemView.findViewById(R.id.delete_image_view);
        }
    }
}
