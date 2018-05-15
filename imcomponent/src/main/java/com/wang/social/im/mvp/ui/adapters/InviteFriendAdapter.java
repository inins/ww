package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
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
import com.wang.social.im.mvp.model.entities.IndexFriendInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.yokeyword.indexablerv.IndexableAdapter;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-12 15:23
 * ============================================
 */
public class InviteFriendAdapter extends IndexableAdapter<IndexFriendInfo> {

    private Context context;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;

    public InviteFriendAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        imageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
    }

    public List<IndexFriendInfo> getSelectItems() {
        List<IndexFriendInfo> list = new ArrayList<>();
        for (IndexFriendInfo friendInfo : getItems()) {
            if (friendInfo.isSelected()) {
                list.add(friendInfo);
            }
        }
        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.im_index_friends, parent, false);
        return new IndexViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.im_item_invite_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
        ((IndexViewHolder) holder).tvIndex.setText(indexTitle);
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, IndexFriendInfo entity) {
        FriendViewHolder viewHolder = (FriendViewHolder) holder;
        viewHolder.cbCheck.setChecked(entity.isSelected());
        imageLoader.loadImage(context, ImageConfigImpl.builder()
                .placeholder(R.drawable.common_default_circle_placeholder)
                .errorPic(R.drawable.common_default_circle_placeholder)
                .isCircle(true)
                .imageView(viewHolder.ivPortrait)
                .url(entity.getPortrait())
                .build());
        viewHolder.tvNickname.setText(entity.getNickname());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String birYear = String.valueOf(year - entity.getAge());
        if (birYear.length() > 2) {
            String range = birYear.charAt(birYear.length() - 2) + "0后";
            viewHolder.tvAge.setText(range);
        }
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
    }

    private class IndexViewHolder extends RecyclerView.ViewHolder {

        private TextView tvIndex;

        public IndexViewHolder(View itemView) {
            super(itemView);
            tvIndex = itemView.findViewById(R.id.tv_index);
        }
    }

    private class FriendViewHolder extends RecyclerView.ViewHolder {

        private AppCompatCheckBox cbCheck;
        private ImageView ivPortrait;
        private TextView tvNickname;
        private TextView tvAge;
        private TextView tvConstellation;
        private TextView tvTags;

        public FriendViewHolder(View itemView) {
            super(itemView);
            cbCheck = itemView.findViewById(R.id.iif_cb_check);
            ivPortrait = itemView.findViewById(R.id.if_iv_portrait);
            tvNickname = itemView.findViewById(R.id.if_tv_nickname);
            tvAge = itemView.findViewById(R.id.if_tv_age);
            tvConstellation = itemView.findViewById(R.id.if_tv_constellation);
            tvTags = itemView.findViewById(R.id.if_tv_tags);
        }
    }
}
