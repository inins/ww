package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.TeamInfo;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-21 18:17
 * ============================================
 */
public class JoinedTeamListAdapter extends BaseAdapter<TeamInfo> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new ViewHolder(context, parent);
    }

    class ViewHolder extends BaseViewHolder<TeamInfo> {

        @BindView(R2.id.it_iv_avatar)
        ImageView itIvAvatar;
        @BindView(R2.id.it_iv_unread)
        ImageView itIvUnread;
        @BindView(R2.id.it_tv_name)
        TextView itTvName;

        ImageLoader mImageLoader;

        public ViewHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.im_item_team);
            mImageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
        }

        @Override
        protected void bindData(TeamInfo itemValue, int position, OnItemClickListener onItemClickListener) {
            mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                    .placeholder(R.drawable.common_default_circle_placeholder)
                    .errorPic(R.drawable.common_default_circle_placeholder)
                    .isCircle(true)
                    .imageView(itIvAvatar)
                    .url(itemValue.getCover())
                    .build());

            itTvName.setText(itemValue.getName());

            TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, itemValue.getTeamId());
            TIMConversationExt conversationExt = new TIMConversationExt(conversation);
            if (conversationExt.getUnreadMessageNum() > 0) {
                itIvUnread.setVisibility(View.VISIBLE);
            } else {
                itIvUnread.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itTvName.setTextColor(Color.WHITE);
                    itemView.setBackgroundResource(R.drawable.common_bg_btn_theme_normal);
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(itemValue, position);
                    }
                }
            });
        }
    }
}
