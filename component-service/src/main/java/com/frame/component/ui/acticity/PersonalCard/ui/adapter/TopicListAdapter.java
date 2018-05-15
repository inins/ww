package com.frame.component.ui.acticity.PersonalCard.ui.adapter;

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
import com.frame.component.entities.Topic;
import com.frame.component.helper.NetZanHelper;
import com.frame.component.service.R;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.component.ui.acticity.tags.TagAdapter;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;
import com.frame.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    public interface ClickListener {
        void onTopicClick(Topic topic);
    }

    private IView mIView;
    private Context mContext;
    private List<Topic> mList;
    private ClickListener mClickListener;

    public TopicListAdapter(IView bindView, RecyclerView recyclerView, List<Topic> list) {
        mIView = bindView;
        mContext = recyclerView.getContext().getApplicationContext();
        mList = list;
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.personal_card_adapter_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mList) return;
        if (position < 0 || position >= mList.size()) return;

        Topic topic = mList.get(position);

        if (null == topic) return;

        // 是否付费
        if (topic.getRelateState() == 0) {
            holder.payFlagIV.setVisibility(View.GONE);
            holder.blankView.setVisibility(View.GONE);
        } else {
            holder.payFlagIV.setVisibility(View.VISIBLE);
            holder.blankView.setVisibility(View.INVISIBLE);
        }
        // 创建时间
        holder.createDateTV.setText(formatCreateDate(mContext, topic.getCreateTime()));
        // 话题标题
        holder.titleTV.setText(topic.getTitle());
        // 简要
        holder.contentTV.setText(topic.getFirstStrff());
        // 图片
        holder.imageView.setVisibility(View.INVISIBLE);
        if (!TextUtils.isEmpty(topic.getBackgroundImage())) {
            holder.imageView.setVisibility(View.VISIBLE);
            FrameUtils.obtainAppComponentFromContext(mContext)
                    .imageLoader()
                    .loadImage(mContext,
                            ImageConfigImpl.builder()
                                    .imageView(holder.imageView)
                                    .url(topic.getBackgroundImage())
                                    .transformation(new RoundedCorners(SizeUtils.dp2px(8)))
                                    .build());
        }
        // 用户头像
        holder.avatarIV.setVisibility(View.INVISIBLE);
        if (!TextUtils.isEmpty(topic.getAvatar())) {
            holder.avatarIV.setVisibility(View.VISIBLE);
            FrameUtils.obtainAppComponentFromContext(mContext)
                    .imageLoader()
                    .loadImage(mContext,
                            ImageConfigImpl.builder()
                                    .imageView(holder.avatarIV)
                                    .url(topic.getAvatar())
                                    .isCircle(true)
                                    .build());
        }
        // 用户昵称
        holder.userNameTV.setText(topic.getNickname());
        // 是否点赞
        if (topic.isSupport()) {
            holder.praiseIV.setImageResource(R.drawable.common_ic_zan_hot);
        } else {
            holder.praiseIV.setImageResource(R.drawable.common_ic_zan);
        }
        holder.praiseIV.setTag(topic);
        holder.praiseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() instanceof Topic) {
                    Topic t = (Topic) v.getTag();

                    NetZanHelper.newInstance()
                            .topicZan(mIView, holder.praiseTV, t.getTopicId(), !t.isSupport(),
                                    new NetZanHelper.OnZanCallback() {
                                        @Override
                                        public void onSuccess(boolean isZan, int zanCount) {
                                            t.setSupportTotal(zanCount);
                                            t.setSupport(isZan);

                                            notifyDataSetChanged();
                                        }
                                    });
                }
            }
        });
        // 点赞次数
        holder.praiseTV.setText(String.format("%d", topic.getSupportTotal()));
        // 评论次数
        holder.commentTV.setText(String.format("%d", topic.getCommentTotal()));
        // 阅读次数
        holder.readTV.setText(String.format("%d", topic.getReadTotal()));
        // 标签
        if (topic.getTags() != null) {
            for (int i = 0; i < Math.min(topic.getTags().size(), 3); i++) {
                Tag tag = topic.getTags().get(i);
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
    }

    public String formatCreateDate(Context context, long mills) {
        String dateString;
        Date date = new Date();
//        long mills = TimeUtils.string2Millis(topic.getCreateTime());
        date.setTime(mills);
        if (TimeUtils.isToday(date)) {
            dateString = "今天" +
                    new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
        } else {
            dateString = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(date);
        }

        return dateString;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
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
        ImageView praiseIV;
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
            praiseIV = itemView.findViewById(R.id.praise_image_view);
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


    /**
     * 获取数据集
     */
    public List<Topic> getResults() {
        return mList;
    }

    /**
     * 刷新数据
     */
    public void refreshData(List<Topic> valueList) {
        mList.clear();
        mList.addAll(valueList);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     */
    public void addItem(List<Topic> items) {
        mList.addAll(items);
        notifyItemInserted(this.mList.size() - items.size());
        notifyItemRangeChanged(this.mList.size() - items.size(), this.mList.size() - 1);
    }
}
