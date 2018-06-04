package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.enums.Gender;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.wang.social.im.R;
import com.wang.social.im.helper.ImHelper;
import com.wang.social.im.mvp.model.entities.IndexFriendInfo;
import com.wang.social.im.view.SwipeMenuLayout;
import com.wang.social.im.view.indexlist.IndexableAdapter;

import java.util.Calendar;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-08 10:24
 * ============================================
 */
public class FriendsAdapter extends IndexableAdapter<IndexFriendInfo> {

    private Context context;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;

    private OnHandleListener handleListener;

    public FriendsAdapter(Context context, OnHandleListener handleListener) {
        this.context = context;
        this.handleListener = handleListener;
        mInflater = LayoutInflater.from(context);
        imageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.im_index_friends, parent, false);
        return new IndexViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.im_item_friends, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle, boolean isSticky) {
        ((IndexViewHolder) holder).tvIndex.setText(indexTitle);
        if (isSticky) {
            ((IndexViewHolder) holder).tvIndex.setTextColor(ContextCompat.getColor(((IndexViewHolder) holder).tvIndex.getContext(), R.color.common_colorAccent));
        } else {
            ((IndexViewHolder) holder).tvIndex.setTextColor(ContextCompat.getColor(((IndexViewHolder) holder).tvIndex.getContext(), R.color.common_text_blank));
        }
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, IndexFriendInfo entity) {
        FriendViewHolder viewHolder = (FriendViewHolder) holder;
        ((SwipeMenuLayout) viewHolder.itemView).quickClose();
        imageLoader.loadImage(context, ImageConfigImpl.builder()
                .placeholder(R.drawable.common_default_circle_placeholder)
                .errorPic(R.drawable.common_default_circle_placeholder)
                .isCircle(true)
                .imageView(viewHolder.ivPortrait)
                .url(entity.getPortrait())
                .build());
        viewHolder.tvNickname.setText(entity.getNickname());
        viewHolder.tvAge.setText(ImHelper.getAgeRange(entity.getBirthday()));
        Drawable genderDrawable;
        if (entity.getGender() == Gender.MALE) {
            viewHolder.tvAge.setBackgroundResource(R.drawable.im_bg_male);
            genderDrawable = ContextCompat.getDrawable(context, R.drawable.common_ic_man);
        } else {
            viewHolder.tvAge.setBackgroundResource(R.drawable.im_bg_female);
            genderDrawable = ContextCompat.getDrawable(context, R.drawable.common_ic_women);
        }
        genderDrawable.setBounds(0, 0, genderDrawable.getMinimumWidth(), genderDrawable.getMinimumHeight());
        viewHolder.tvAge.setCompoundDrawables(genderDrawable, null, null, null);
        viewHolder.tvConstellation.setText(entity.getConstellation());
        String tags = "";
        for (Tag tag : entity.getTags()) {
            tags = tags + "#" + tag.getTagName() + " ";
        }
        viewHolder.tvTags.setText(tags);
        viewHolder.tvbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SwipeMenuLayout) viewHolder.itemView).smoothClose();
                if (handleListener != null) {
                    handleListener.onDelete(entity);
                }
            }
        });
        viewHolder.clContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handleListener != null) {
                    handleListener.onItemClick(entity);
                }
            }
        });
    }

    private class IndexViewHolder extends RecyclerView.ViewHolder {

        private TextView tvIndex;

        public IndexViewHolder(View itemView) {
            super(itemView);
            tvIndex = itemView.findViewById(R.id.tv_index);
        }
    }

    private class FriendViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPortrait;
        private TextView tvNickname;
        private TextView tvAge;
        private TextView tvConstellation;
        private TextView tvTags;
        private TextView tvbDelete;
        private ConstraintLayout clContent;

        public FriendViewHolder(View itemView) {
            super(itemView);
            ivPortrait = itemView.findViewById(R.id.if_iv_portrait);
            tvNickname = itemView.findViewById(R.id.if_tv_nickname);
            tvAge = itemView.findViewById(R.id.if_tv_age);
            tvConstellation = itemView.findViewById(R.id.if_tv_constellation);
            tvTags = itemView.findViewById(R.id.if_tv_tags);
            tvbDelete = itemView.findViewById(R.id.im_tvb_delete);
            clContent = itemView.findViewById(R.id.if_cl_content);
        }
    }

    public interface OnHandleListener {

        void onDelete(IndexFriendInfo friendInfo);

        void onItemClick(IndexFriendInfo friendInfo);
    }
}
