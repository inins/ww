package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.frame.component.entities.ShareUserInfo;

import java.util.Locale;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-14 15:09
 * ============================================
 */
public class ShareListAdapter extends BaseAdapter<ShareUserInfo> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new ViewHolder(context, parent);
    }

    class ViewHolder extends BaseViewHolder<ShareUserInfo> {

        @BindView(R2.id.isl_tv_rank)
        TextView islTvRank;
        @BindView(R2.id.isl_iv_portrait)
        ImageView islIvPortrait;
        @BindView(R2.id.isl_tv_nickname)
        TextView islTvNickname;
        @BindView(R2.id.isl_tv_referrals)
        TextView islTvReferrals;

        private ImageLoader imageLoader;

        public ViewHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.im_item_share_list);
            imageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
        }

        @Override
        protected void bindData(ShareUserInfo itemValue, int position, OnItemClickListener onItemClickListener) {
            islTvRank.setText(String.valueOf(position + 1));
            islTvNickname.setText(itemValue.getNickname());
            islTvReferrals.setText(String.format(Locale.getDefault(), "%d位下线", itemValue.getReferrals()));
            imageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                    .placeholder(R.drawable.common_default_circle_placeholder)
                    .errorPic(R.drawable.common_default_circle_placeholder)
                    .isCircle(true)
                    .imageView(islIvPortrait)
                    .url(itemValue.getPortrait())
                    .build());
        }
    }
}