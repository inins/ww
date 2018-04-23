package com.wang.social.topic.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.social.topic.R;

public class TopUserAdapter extends RecyclerView.Adapter<TopUserAdapter.ViewHolder> {

    private Context mContext;

    public TopUserAdapter(RecyclerView recyclerView) {
        mContext = recyclerView.getContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.topic_adapter_top_user, parent, false);
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
        ImageView mAvatarIV;
        TextView mNameTV;
        ImageView mGenderIV;
        TextView mPropertyTV;
        TextView mTopicCountTV;
        TextView mZodiacTV;
        TextView mTagsTV;

        public ViewHolder(View itemView) {
            super(itemView);

            mAvatarIV = itemView.findViewById(R.id.avatar_image_view);
            mNameTV = itemView.findViewById(R.id.name_text_view);
            mGenderIV = itemView.findViewById(R.id.gender_image_view);
            mPropertyTV = itemView.findViewById(R.id.property_text_view);
            mTopicCountTV = itemView.findViewById(R.id.topic_count_text_view);
            mZodiacTV = itemView.findViewById(R.id.zodiac_text_view);
            mTagsTV = itemView.findViewById(R.id.tags_text_view);
        }
    }
}
