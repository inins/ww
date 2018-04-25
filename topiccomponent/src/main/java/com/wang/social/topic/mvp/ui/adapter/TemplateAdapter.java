package com.wang.social.topic.mvp.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;
import com.wang.social.topic.R;
import com.wang.social.topic.mvp.model.entities.Template;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    Context mContext;
    DataProvider mDataProvider;
    ClickListener mClickListener;

    public interface DataProvider {
        Template getTemplate(int position);
        int getTemplateCount();
        boolean isSelected(int id);
    }

    public interface ClickListener {
        void onTemplateClick(Template template);
    }

    public TemplateAdapter(RecyclerView recyclerView) {
        this.mContext = recyclerView.getContext().getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.topic_adapter_template, parent, false);
        return new ViewHolder(view);
    }

    public void setDataProvider(DataProvider dataProvider) {
        this.mDataProvider = dataProvider;
    }

    public void setClickListener(ClickListener listener) {
        this.mClickListener = listener;
    }

    private int getTemplateCount() {
        if (null != mDataProvider) {
            return mDataProvider.getTemplateCount();
        }

        return 0;
    }

    private Template getTemplate(int position) {
        if (null != mDataProvider) {
            return mDataProvider.getTemplate(position);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Template t = getTemplate(position);

        if (null == t) return;

        // 默认
        if (t.getId() == -1) {
            holder.descTV.setVisibility(View.VISIBLE);
            holder.templateIV.setImageDrawable(new ColorDrawable(Color.WHITE));
        } else {
            // 模板图片
            if (!TextUtils.isEmpty(t.getUrl())) {
                FrameUtils.obtainAppComponentFromContext(mContext)
                        .imageLoader()
                        .loadImage(mContext,
                                ImageConfigImpl.builder()
                                        .imageView(holder.templateIV)
                                        .url(t.getUrl())
                                        .transformation(new RoundedCorners(SizeUtils.dp2px(8)))
                                        .build());
            }
        }
        // 名字
        holder.nameTV.setText(t.getName());
        // 是否选中
        if (null != mDataProvider && mDataProvider.isSelected(t.getId())) {
            holder.flagIV.setVisibility(View.VISIBLE);
        } else {
            holder.flagIV.setVisibility(View.INVISIBLE);
        }
        // 点击
        holder.rootView.setTag(t);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null != mClickListener && v.getTag() instanceof Template) {
                    mClickListener.onTemplateClick((Template) v.getTag());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return getTemplateCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView templateIV;
        ImageView flagIV;
        TextView nameTV;
        TextView descTV;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            templateIV = itemView.findViewById(R.id.template_image_view);
            flagIV = itemView.findViewById(R.id.flag_image_view);
            nameTV = itemView.findViewById(R.id.name_text_view);
            descTV = itemView.findViewById(R.id.desc_text_view);
        }
    }
}
