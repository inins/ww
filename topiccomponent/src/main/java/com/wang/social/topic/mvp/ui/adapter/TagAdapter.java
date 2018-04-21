package com.wang.social.topic.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wang.social.topic.R;
import com.wang.social.topic.mvp.model.entities.Tag;

import java.lang.ref.WeakReference;
import java.util.List;

import timber.log.Timber;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {


    private Context mContext;
    List<Tag> mTagList;

    public TagAdapter(Context context, List<Tag> list) {
        this.mContext = context.getApplicationContext();
        mTagList = list;
    }

    private int getTagCount() {
        return null == mTagList ? 0 : mTagList.size();
    }

    private String getTagName(int position) {
        if (null != mTagList && mTagList.size() > position) {
            return mTagList.get(position).getTagName();
        }

        return "";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.topic_adapter_topic_tag, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(getTagName(position));
    }

    @Override
    public int getItemCount() {
        return getTagCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.text_view);
        }
    }
}
