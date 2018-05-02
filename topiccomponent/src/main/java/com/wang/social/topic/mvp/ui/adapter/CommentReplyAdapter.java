package com.wang.social.topic.mvp.ui.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.wang.social.topic.mvp.model.entities.Comment;

import java.util.List;

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.ViewHolder> {

    public interface ClickListener {
        void onReply(Comment comment);
        void onSupport(Comment comment);
    }

    Context mContext;
    List<Comment> mCommentList;
    ClickListener mClickListener;

    public CommentReplyAdapter(RecyclerView recyclerView, List<Comment> list) {
        mContext = recyclerView.getContext().getApplicationContext();
        mCommentList = list;
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.topic_adapter_comment_reply_list, parent, false);
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

        if (position == 0) {
//            holder.divider0.setVisibility(View.VISIBLE);
            holder.divider1.setVisibility(View.GONE);
            holder.rootView.setBackgroundColor(Color.WHITE);

            holder.supportCountTV.setVisibility(View.GONE);
            holder.supportIV.setVisibility(View.GONE);
        } else {
//            holder.divider0.setVisibility(View.GONE);
//            holder.divider1.setVisibility(View.VISIBLE);

        }

        // 头像
        if (TextUtils.isEmpty(comment.getAvatar())) {
            FrameUtils.obtainAppComponentFromContext(mContext)
                    .imageLoader()
                    .loadImage(mContext,
                            ImageConfigImpl.builder()
                            .imageView(holder.avatarIV)
                            .url(comment.getAvatar())
                            .build());
        }
        // 昵称
        holder.nickNameTV.setText(comment.getNickname());
        // 点赞数
        holder.supportCountTV.setText(comment.getSupportTotal().toString());
        // 点赞
        holder.supportIV.setTag(comment);
        holder.supportIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() instanceof Comment) {
                    if (null != mClickListener) {
                        mClickListener.onSupport((Comment)v.getTag());
                    }
                }
            }
        });
        // 内容
        if (comment.getTargetUserId() > 0) {
            // 回复别人的内容
            String[] strings = {
                    mContext.getResources().getString(R.string.topic_reply),
                    "@" + (TextUtils.isEmpty(comment.getTargetNickname()) ?
                            comment.getTargetUserId() : comment.getTargetNickname()),
                    comment.getContent()
            };
            int[] colors = {
                    ContextCompat.getColor(mContext, R.color.common_text_blank),
                    ContextCompat.getColor(mContext, R.color.common_blue_deep),
                    ContextCompat.getColor(mContext, R.color.common_text_blank)
            };
            SpannableStringBuilder spanText = SpannableStringUtil.createV2(strings, colors);
            holder.contentTV.setText(spanText);
        } else {
            holder.contentTV.setText(comment.getContent());
        }

        holder.rootView.setTag(comment);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() instanceof Comment) {
                    if (null != mClickListener) {
                        mClickListener.onReply((Comment)v.getTag());
                    }
                }
            }
        });
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
        View divider0;
        View divider1;


        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            avatarIV = itemView.findViewById(R.id.avatar_image_view);
            nickNameTV = itemView.findViewById(R.id.nick_name_text_view);
            supportIV = itemView.findViewById(R.id.support_image_view);
            supportCountTV = itemView.findViewById(R.id.support_text_view);
            createDateTV = itemView.findViewById(R.id.create_date_text_view);
            contentTV = itemView.findViewById(R.id.content_text_view);
            divider0 = itemView.findViewById(R.id.divider_0);
            divider1 = itemView.findViewById(R.id.divider_1);
        }
    }
}
