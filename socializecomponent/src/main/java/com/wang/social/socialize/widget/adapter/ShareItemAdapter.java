package com.wang.social.socialize.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.social.socialize.R;

public class ShareItemAdapter extends RecyclerView.Adapter<ShareItemAdapter.ViewHolder> {

    public interface DataProvider {
        int getNameRes(int position);
        int getIconRes(int position);
        int getTag();
        int getItemCount();
    }

    public interface ClickListener {
        void onClick(int position);
    }

    private Context mContext;
    private DataProvider dataProvider;
    private ClickListener clickListener;

    public ShareItemAdapter(Context context, DataProvider dataProvider, ClickListener clickListener) {
        this.mContext = context;
        this.dataProvider = dataProvider;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.socialize_adapter_share_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == dataProvider) return;

        holder.nameTV.setText(dataProvider.getNameRes(position));
        holder.iconIV.setBackgroundResource(dataProvider.getIconRes(position));

        holder.rootView.setOnClickListener(
                v -> {
                    if (null != clickListener) {
                        clickListener.onClick(position);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return dataProvider == null ? 0 : dataProvider.getItemCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView nameTV;
        ImageView iconIV;
        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            nameTV = itemView.findViewById(R.id.name_text_view);
            iconIV = itemView.findViewById(R.id.icon_image_view);
        }
    }
}
