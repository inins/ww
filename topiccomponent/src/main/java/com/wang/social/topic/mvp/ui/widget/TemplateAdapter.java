package com.wang.social.topic.mvp.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.social.topic.R;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    Context mContext;

    public TemplateAdapter(RecyclerView recyclerView) {
        this.mContext = recyclerView.getContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.topic_adapter_template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView templateIV;
        ImageView flagIV;
        TextView nameTV;
        TextView descTV;

        public ViewHolder(View itemView) {
            super(itemView);

            templateIV = itemView.findViewById(R.id.template_image_view);
            flagIV = itemView.findViewById(R.id.flag_image_view);
            nameTV = itemView.findViewById(R.id.name_text_view);
            descTV = itemView.findViewById(R.id.desc_text_view);
        }
    }
}
