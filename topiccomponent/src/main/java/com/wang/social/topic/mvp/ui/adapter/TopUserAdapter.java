package com.wang.social.topic.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.CommonHelper;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.frame.utils.TimeUtils;
import com.wang.social.topic.R;
import com.wang.social.topic.utils.StringUtil;
import com.wang.social.topic.mvp.model.entities.Tag;
import com.wang.social.topic.mvp.model.entities.TopicTopUser;

import java.util.List;

public class TopUserAdapter extends RecyclerView.Adapter<TopUserAdapter.ViewHolder> {

    private Context mContext;
    private List<TopicTopUser> mList;

    public TopUserAdapter(RecyclerView recyclerView, List<TopicTopUser> list) {
        mContext = recyclerView.getContext().getApplicationContext();
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.topic_adapter_top_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mList == null) return;
        if (position < 0 || position >= mList.size()) return;

        TopicTopUser user = mList.get(position);

        if (null == user) return;

        // 头像
        if (!TextUtils.isEmpty(user.getAvatar())) {
            FrameUtils.obtainAppComponentFromContext(mContext)
                    .imageLoader()
                    .loadImage(mContext,
                            ImageConfigImpl.builder()
                                    .imageView(holder.mAvatarIV)
                                    .url(user.getAvatar())
                                    .isCircle(true)
                                    .build());
        }
        // 昵称
        holder.mNameTV.setText(user.getNickname());
        // 性别
        if (user.getSex() == 0) {
            // 男
            holder.mGenderLayout.setBackgroundResource(R.drawable.topic_shape_rect_corner_solid_blue_gray);
            holder.mGenderIV.setImageResource(R.drawable.common_ic_man);
        } else {
            // 女
            holder.mGenderLayout.setBackgroundResource(R.drawable.topic_shape_rect_corner_solid_red_gray);
            holder.mGenderIV.setImageResource(R.drawable.common_ic_women);
        }
        // 年代
        holder.mPropertyTV.setText(StringUtil.getBirthYears(user.getBirthday()));
        // 星座
        holder.mZodiacTV.setText(TimeUtils.getAstro(user.getBirthday()));
        // 标签
        String tags = "";
        for (Tag tag : user.getTags()) {
            tags += "#" + tag.getTagName() + "  ";
        }
        holder.mTagsTV.setText(tags);
        // 话题数量
        holder.mTopicCountTV.setText(
                String.format(mContext.getResources().getString(R.string.topic_top_user_comment_count_format),
                        user.getPublishNum()));

        holder.mRootView.setTag(user);
        holder.mRootView.setOnClickListener(v -> {
            if (v.getTag() instanceof TopicTopUser) {
                TopicTopUser tu = (TopicTopUser) v.getTag();

                CommonHelper.ImHelper.startPersonalCardForBrowse(mContext, tu.getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View mRootView;
        ImageView mAvatarIV;
        TextView mNameTV;
        View mGenderLayout;
        ImageView mGenderIV;
        TextView mPropertyTV;
        TextView mTopicCountTV;
        TextView mZodiacTV;
        TextView mTagsTV;

        public ViewHolder(View itemView) {
            super(itemView);

            mRootView = itemView;
            mAvatarIV = itemView.findViewById(R.id.avatar_image_view);
            mNameTV = itemView.findViewById(R.id.name_text_view);
            mGenderLayout = itemView.findViewById(R.id.gender_layout);
            mGenderIV = itemView.findViewById(R.id.gender_image_view);
            mPropertyTV = itemView.findViewById(R.id.property_text_view);
            mTopicCountTV = itemView.findViewById(R.id.topic_count_text_view);
            mZodiacTV = itemView.findViewById(R.id.zodiac_text_view);
            mTagsTV = itemView.findViewById(R.id.tags_text_view);
        }
    }
}
