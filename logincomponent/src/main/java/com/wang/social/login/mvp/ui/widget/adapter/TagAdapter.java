package com.wang.social.login.mvp.ui.widget.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.social.login.R;
import com.wang.social.login.mvp.model.entities.Tag;

import timber.log.Timber;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
    /**
     * 数据提供
     */
    public interface TagDataProvider {
        int getItemCount();
        Tag getItem(int position);
        boolean isDeleteMode();
    }

    public interface TagClickListener {
        void onTagClick(Tag tag);
        void onDelete(Tag tag);
    }

    Context context;
    TagDataProvider dataProvider;
    TagClickListener clickListener;

    public TagAdapter(Context context, TagDataProvider dataProvider, TagClickListener clickListener) {
        this.context = context;
        this.dataProvider = dataProvider;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.login_view_tag, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == dataProvider) return;
        Tag tag = dataProvider.getItem(position);
        if (null == tag) return;

//        Timber.i(tag.getId() + " " + tag.getTagName() + " " + tag.getState());
        // 设置选中和未选中
        if (tag.isPersonalTag() || dataProvider.isDeleteMode()) {
            holder.textLayout.setBackground(
                    context.getResources().
                            getDrawable(R.drawable.login_shape_rect_corner_solid_blue_deep));
            holder.nameTV.setTextColor(Color.WHITE);
        } else {
            holder.textLayout.setBackground(
                    context.getResources().
                            getDrawable(R.drawable.login_shape_rect_corner_stroke_blue_deep));
            holder.nameTV.setTextColor(
                    context.getResources().getColor(R.color.common_blue_deep));
        }
        holder.nameTV.setText(tag.getTagName());

        if (dataProvider.isDeleteMode()) {
            // 删除
            holder.deleteIV.setTag(tag);
            holder.deleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() instanceof Tag) {

                        if (null != clickListener) {
                            clickListener.onDelete((Tag) v.getTag());
                        }
                    }
                }
            });
        } else {
            // 选择模式下删除按钮不可见
            holder.deleteIV.setVisibility(View.GONE);
            // 点击
            holder.textLayout.setTag(tag);
            holder.textLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() instanceof Tag) {

                        if (null != clickListener) {
                            clickListener.onTagClick((Tag) v.getTag());
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataProvider == null ? 0 : dataProvider.getItemCount();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View textLayout;
        TextView nameTV;
        ImageView deleteIV;

        public ViewHolder(View itemView) {
            super(itemView);

            textLayout = itemView.findViewById(R.id.text_layout);
            nameTV = itemView.findViewById(R.id.name_text_view);
            deleteIV = itemView.findViewById(R.id.delete_image_view);
        }
    }
}
