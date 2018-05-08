package com.wang.social.topic.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;
import com.wang.social.topic.R;
import com.wang.social.topic.utils.StringUtil;
import com.wang.social.topic.mvp.model.entities.Topic;
import com.wang.social.topic.mvp.model.entities.TopicTag;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    public interface DataProvider {
        Topic getTopic(int position);

        int getTopicCount();
    }

    public interface ClickListener {
        void onTopicClick(Topic topic);
    }

    Context mContext;
    DataProvider mDataProvider;
    ClickListener mClickListener;

    public TopicListAdapter(Context context, DataProvider dataprovider, ClickListener clickListener) {
        this.mContext = context.getApplicationContext();
        this.mDataProvider = dataprovider;
        this.mClickListener = clickListener;
    }

    public void onDestroy() {
        mDataProvider = null;
        mClickListener = null;
    }

    private Topic getTopic(int position) {
//        Timber.i("getTopic : " + position);
        if (null != mDataProvider) {
            return mDataProvider.getTopic(position);
        }

        return null;
    }

    private int getTopicCount() {
        if (null != mDataProvider) {
            return mDataProvider.getTopicCount();
        }

        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.topic_adapter_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Topic topic = getTopic(position);
        if (null == topic) return;

        // 付费
        if (topic.getIsFree() == 0) {
            holder.payFlagIV.setVisibility(View.GONE);
            holder.blankView.setVisibility(View.GONE);
        } else {
            holder.payFlagIV.setVisibility(View.VISIBLE);
            holder.blankView.setVisibility(View.INVISIBLE);
        }
        // 创建时间
        holder.createDateTV.setText(StringUtil.formatCreateDate(mContext, topic.getCreateTime()));
        // 话题标题
        holder.titleTV.setText(topic.getTitle());
        // 简要
        holder.contentTV.setText(topic.getFirstStrff());
        // 图片
        if (!TextUtils.isEmpty(topic.getTopicImage())) {
            FrameUtils.obtainAppComponentFromContext(mContext)
                    .imageLoader()
                    .loadImage(mContext,
                            ImageConfigImpl.builder()
                                    .imageView(holder.imageView)
                                    .url(topic.getTopicImage())
                                    .transformation(new RoundedCorners(SizeUtils.dp2px(8)))
                                    .build());
        }
        // 用户头像
        if (!TextUtils.isEmpty(topic.getUserCover())) {
            FrameUtils.obtainAppComponentFromContext(mContext)
                    .imageLoader()
                    .loadImage(mContext,
                            ImageConfigImpl.builder()
                                    .imageView(holder.avatarIV)
                                    .url(topic.getUserCover())
                                    .isCircle(true)
                                    .build());
        }
        // 用户昵称
        holder.userNameTV.setText(topic.getUserName());
        // 点赞次数
        holder.praiseTV.setText(String.format("%d", topic.getTopicSupportNum()));
        // 评论次数
        holder.commentTV.setText(String.format("%d", topic.getTopicCommentNum()));
        // 阅读次数
        holder.readTV.setText(String.format("%d", topic.getTopicReadNum()));
        // 标签
        if (topic.getTopicTag() != null) {
            for (int i = 0; i < Math.min(topic.getTopicTag().size(), 3); i++) {
                TopicTag tag = topic.getTopicTag().get(i);
                if (null == tag) continue;
                if (!TextUtils.isEmpty(tag.getTagName())) {
                    switch (i) {
                        case 0:
                            holder.tag1TV.setVisibility(View.VISIBLE);
                            holder.tag1TV.setText(tag.getTagName());
                            break;
                        case 1:
                            holder.tag2TV.setVisibility(View.VISIBLE);
                            holder.tag2TV.setText(tag.getTagName());
                            break;
                        case 2:
                            holder.tag3TV.setVisibility(View.VISIBLE);
                            holder.tag3TV.setText(tag.getTagName());
                            break;
                    }
                }
            }
        }

        // 点击
        holder.rootView.setTag(topic);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mClickListener && v.getTag() instanceof Topic) {
                    mClickListener.onTopicClick((Topic) v.getTag());
                }
            }
        });
        holder.praiseCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    @Override
    public int getItemCount() {
//        Timber.i("getItemCount : " + getTopicCount());
        return getTopicCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView payFlagIV;
        View blankView;
        TextView createDateTV;
        TextView titleTV;
        TextView contentTV;
        ImageView avatarIV;
        TextView userNameTV;
        ImageView imageView;
        CheckBox praiseCB;
        TextView praiseTV;
        ImageView commentIV;
        TextView commentTV;
        ImageView readIV;
        TextView readTV;
        TextView tag1TV;
        TextView tag2TV;
        TextView tag3TV;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            payFlagIV = itemView.findViewById(R.id.pay_flag_image_view);
            blankView = itemView.findViewById(R.id.blank_view);
            createDateTV = itemView.findViewById(R.id.create_date_text_view);
            titleTV = itemView.findViewById(R.id.title_text_view);
            contentTV = itemView.findViewById(R.id.content_text_view);
            avatarIV = itemView.findViewById(R.id.avatar_image_view);
            userNameTV = itemView.findViewById(R.id.user_name_text_view);
            imageView = itemView.findViewById(R.id.image_view);
            praiseCB = itemView.findViewById(R.id.praise_checkbox);
            praiseTV = itemView.findViewById(R.id.praise_text_view);
            commentIV = itemView.findViewById(R.id.comment_image_view);
            commentTV = itemView.findViewById(R.id.comment_text_view);
            readIV = itemView.findViewById(R.id.read_image_view);
            readTV = itemView.findViewById(R.id.read_text_view);
            tag1TV = itemView.findViewById(R.id.topic_tag_1_text_view);
            tag2TV = itemView.findViewById(R.id.topic_tag_2_text_view);
            tag3TV = itemView.findViewById(R.id.topic_tag_3_text_view);
        }
    }
}
