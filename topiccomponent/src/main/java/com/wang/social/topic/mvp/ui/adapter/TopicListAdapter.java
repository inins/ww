package com.wang.social.topic.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.frame.utils.TimeUtils;
import com.wang.social.topic.R;
import com.wang.social.topic.mvp.model.entities.Tag;
import com.wang.social.topic.mvp.model.entities.Topic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    public interface DataProvider {
        Topic getTopic(int position);
        int getTopicCount();
    }

    public interface ClickListener {
        void onTopicClick();
    }

    Context mContext;
    DataProvider mDataProvider;
    ClickListener mClickListener;

    List<Tag> list = new ArrayList<>();

    public TopicListAdapter(Context context, DataProvider dataprovider, ClickListener clickListener) {
        this.mContext = context.getApplicationContext();
        this.mDataProvider = dataprovider;
        this.mClickListener = clickListener;

        for (int i = 0; i <5 ; i++) {
            Tag tag = new Tag();
            tag.setTagName("TAG" + i);
            list.add(tag);
        }
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
        if (topic.getIsFree().equals("0")) {
            holder.payFlagIV.setVisibility(View.GONE);
        } else {
            holder.payFlagIV.setVisibility(View.VISIBLE);
        }
        // 创建时间
        Date date = new Date();
        long mills = TimeUtils.string2Millis(topic.getCreateTime());
        date.setTime(mills);
        String dateString;
        if (TimeUtils.isToday(date)) {
            dateString = mContext.getString(R.string.topic_today) +
                    new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
        } else {
            dateString = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(date);
        }
        holder.createDateTV.setText(dateString);
        // 话题标题
        holder.titleTV.setText(topic.getTitle());
        // 简要
        holder.contentTV.setText(topic.getFirstStrff());
        // 用户头像
        FrameUtils.obtainAppComponentFromContext(mContext)
                .imageLoader()
                .loadImage(mContext,
                        ImageConfigImpl.builder()
                                .imageView(holder.avatarIV)
                                .url(topic.getUserCover())
                                .build());
        // 用户昵称
        holder.userNameTV.setText(topic.getUserName());
        // 点赞次数
        holder.praiseTV.setText(topic.getTopicSupportNum());
        // 评论次数
        holder.commentTV.setText(topic.getTopicCommentNum());
        // 阅读次数
        holder.readTV.setText(topic.getTopicReadNum());
        // 标签
//        if (topic.getTags() != null && topic.getTags().size() > 0) {
//            holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//            holder.recyclerView.setAdapter(new TagAdapter(mContext, topic.getTags()));
//        }
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(new TagAdapter(mContext, list));

        // 点击
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            payFlagIV = itemView.findViewById(R.id.pay_flag_image_view);
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
            recyclerView = itemView.findViewById(R.id.tag_recycler_view);
        }
    }
}
