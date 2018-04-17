package com.wang.social.topic.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wang.social.topic.R;

import java.lang.ref.WeakReference;

public class SelectedTagAdapter extends RecyclerView.Adapter<SelectedTagAdapter.ViewHolder> {

    public interface DataProvider {
        int getItemCount();
        String getName(int position);
    }

    private Context mContext;
    private WeakReference<DataProvider> mDataProvider;

    public SelectedTagAdapter(Context context, DataProvider dataProvider) {
        this.mContext = context.getApplicationContext();
        this.mDataProvider = new WeakReference<>(dataProvider);
    }

    private int getTagCount() {
        if (null == mDataProvider) return 0;
        if (null == mDataProvider.get()) return 0;
        return mDataProvider.get().getItemCount();
    }

    private String getTagName(int position) {
        if (null == mDataProvider) return "";
        if (null == mDataProvider.get()) return "";

        return mDataProvider.get().getName(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.topic_adapter_selectd_tag, parent, false);

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
