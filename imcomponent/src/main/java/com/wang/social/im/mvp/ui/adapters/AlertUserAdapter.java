package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.IndexMemberInfo;

import me.yokeyword.indexablerv.IndexableAdapter;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-12 17:31
 * ============================================
 */
public class AlertUserAdapter extends IndexableAdapter<IndexMemberInfo> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;

    public AlertUserAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mImageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.im_index_friends, parent, false);
        return new IndexViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.im_item_alert_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
        ((IndexViewHolder) holder).tvIndex.setText(indexTitle);
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, IndexMemberInfo entity) {
        mImageLoader.loadImage(mContext, ImageConfigImpl.builder()
                .placeholder(R.drawable.common_default_circle_placeholder)
                .errorPic(R.drawable.common_default_circle_placeholder)
                .isCircle(true)
                .imageView(((ViewHolder) holder).ivPortrait)
                .url(entity.getPortrait())
                .build());

        ((ViewHolder) holder).tvNickname.setText(entity.getNickname());
    }

    private class IndexViewHolder extends RecyclerView.ViewHolder {

        private TextView tvIndex;

        public IndexViewHolder(View itemView) {
            super(itemView);
            tvIndex = itemView.findViewById(R.id.tv_index);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPortrait;
        private TextView tvNickname;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPortrait = itemView.findViewById(R.id.iau_iv_portrait);
            tvNickname = itemView.findViewById(R.id.iau_tv_nickname);
        }
    }
}
