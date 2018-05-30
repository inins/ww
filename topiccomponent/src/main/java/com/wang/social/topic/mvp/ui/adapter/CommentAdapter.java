package com.wang.social.topic.mvp.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.utils.SpannableStringUtil;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.wang.social.topic.R;
import com.wang.social.topic.utils.StringUtil;
import com.wang.social.topic.mvp.model.entities.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    public interface ClickListener {
        void onOpenReplyList(Comment comment);

        void onReply(Comment comment);

        void onSupport(Comment comment);
    }

    private Context mContext;
    private List<Comment> mCommentList;
    private ClickListener mClickListener;


    public CommentAdapter(RecyclerView recyclerView, List<Comment> list) {
        mContext = recyclerView.getContext().getApplicationContext();
        mCommentList = list;
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.topic_adapter_comment_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mCommentList) return;
        if (position < 0 || position >= mCommentList.size()) {
            return;
        }

        Comment comment = mCommentList.get(position);
        if (null == comment) return;

        // 头像
        holder.avatarIV.setVisibility(View.INVISIBLE);
        if (!TextUtils.isEmpty(comment.getAvatar())) {
            holder.avatarIV.setVisibility(View.VISIBLE);
            FrameUtils.obtainAppComponentFromContext(mContext)
                    .imageLoader()
                    .loadImage(mContext,
                            ImageConfigImpl.builder()
                                    .imageView(holder.avatarIV)
                                    .url(comment.getAvatar())
                                    .isCircle(true)
                                    .build());
        }
        // 创建日期
        holder.createDateTV.setText(StringUtil.formatCreateDate(mContext, comment.getCreateTime()));
        // 昵称
        holder.nickNameTV.setText(comment.getNickname());
        // 点赞
        if (comment.getIsSupport() == 0) {
            holder.supportIV.setImageResource(R.drawable.common_ic_zan);
        } else {
            holder.supportIV.setImageResource(R.drawable.common_ic_zan_hot);
        }
        holder.supportIV.setTag(comment);
        holder.supportIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() instanceof Comment) {
                    support(comment);
                }
            }
        });
        // 点赞数量
        holder.supportCountTV.setText(Integer.toString(comment.getSupportTotal()));
        // 内容
        holder.contentTV.setText(comment.getContent());
        // 评论
        if (comment.getReplyNum() <= 0) {
            // 没有评论
            holder.replyTV.setVisibility(View.GONE);
        } else {
            Comment reply = comment.getCommentReply().get(0);
            if (null == reply) return;

            holder.replyTV.setVisibility(View.VISIBLE);

            if (comment.getReplyNum() > 1) {
                // 多于一条，显示数量
                String[] strings = {
                        reply.getNickname(),
                        mContext.getResources().getString(R.string.topic_et_al),
                        String.format(
                                mContext.getResources().getString(R.string.topic_total_reply_count_format),
                                comment.getReplyNum())};
                int[] colors = {
                        ContextCompat.getColor(mContext, R.color.common_blue_deep),
                        ContextCompat.getColor(mContext, R.color.common_text_blank),
                        ContextCompat.getColor(mContext, R.color.common_blue_deep)
                };
                SpannableStringBuilder spanText = SpannableStringUtil.createV2(strings, colors);
                holder.replyTV.setText(spanText);
            } else {
                // 只有一条，直接显示
                String[] strings = {
                        reply.getNickname(),
                        " : ",
                        reply.getContent()
                };
                int[] colors = {
                        ContextCompat.getColor(mContext, R.color.common_blue_deep),
                        ContextCompat.getColor(mContext, R.color.common_text_blank),
                        ContextCompat.getColor(mContext, R.color.common_text_blank)
                };
                SpannableStringBuilder spanText = SpannableStringUtil.createV2(strings, colors);
                holder.replyTV.setText(spanText);
            }

            holder.replyTV.setTag(comment);
            holder.replyTV.setClickable(true);
            holder.replyTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() instanceof Comment) {
                        // 跳转到二级页面
                        openReplyList((Comment) v.getTag());
                    }
                }
            });
        }

        // 点击回复
        holder.rootView.setTag(comment);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() instanceof Comment) {
                    reply((Comment) v.getTag());
                }
            }
        });
    }

    private void openReplyList(Comment comment) {
        if (null != mClickListener) {
            mClickListener.onOpenReplyList(comment);
        }
    }

    private void reply(Comment comment) {
        if (null != mClickListener) {
            mClickListener.onReply(comment);
        }
    }

    private void support(Comment comment) {
        if (null != mClickListener) {
            mClickListener.onSupport(comment);
        }
    }

    @Override
    public int getItemCount() {
        return null == mCommentList ? 0 : mCommentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView avatarIV;
        TextView nickNameTV;
        ImageView supportIV;
        TextView supportCountTV;
        TextView createDateTV;
        TextView contentTV;
        TextView replyTV;


        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            avatarIV = itemView.findViewById(R.id.avatar_image_view);
            nickNameTV = itemView.findViewById(R.id.nick_name_text_view);
            supportIV = itemView.findViewById(R.id.support_image_view);
            supportCountTV = itemView.findViewById(R.id.support_text_view);
            createDateTV = itemView.findViewById(R.id.create_date_text_view);
            contentTV = itemView.findViewById(R.id.content_text_view);
            replyTV = itemView.findViewById(R.id.reply_text_view);
        }
    }
}
