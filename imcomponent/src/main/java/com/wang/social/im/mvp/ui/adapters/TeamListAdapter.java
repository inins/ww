package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.TeamInfo;

import java.util.Locale;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-21 18:54
 * ============================================
 */
public class TeamListAdapter extends BaseAdapter<TeamInfo> {
    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new ViewHolder(context, parent);
    }

    class ViewHolder extends BaseViewHolder<TeamInfo> {

        @BindView(R.id.tl_iv_avatar)
        ImageView tlIvAvatar;
        @BindView(R.id.tl_tv_name)
        TextView tlTvName;
        @BindView(R.id.tl_tv_tag)
        TextView tlTvTag;
        @BindView(R.id.tl_tv_member)
        TextView tlTvMember;
        @BindView(R.id.tl_tvb_join)
        TextView tlTvbJoin;

        ImageLoader mImageLoader;

        public ViewHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.im_item_team_list);
            mImageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
        }

        @Override
        protected void bindData(TeamInfo itemValue, int position, OnItemClickListener onItemClickListener) {
            mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                    .placeholder(R.drawable.common_default_circle_placeholder)
                    .errorPic(R.drawable.common_default_circle_placeholder)
                    .isCircle(true)
                    .imageView(tlIvAvatar)
                    .url(itemValue.getCover())
                    .build());

            tlTvName.setText(itemValue.getName());

            if (itemValue.getTags() != null && itemValue.getTags().size() > 0) {
                tlTvTag.setText(itemValue.getTags().get(0).getTagName());
            } else {
                tlTvTag.setVisibility(View.GONE);
            }

            tlTvMember.setText(String.format(Locale.getDefault(), "成员%d人", itemValue.getMemberSize()));

            if (itemValue.isJoined()) {
                tlTvbJoin.setText(R.string.im_joined);
                tlTvbJoin.setEnabled(false);
            } else {
                tlTvbJoin.setEnabled(true);
                tlTvbJoin.setText(R.string.im_join);
            }
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }
}
