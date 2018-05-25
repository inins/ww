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
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.UIConversation;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-25 11:35
 * ============================================
 */
public class RecentlyAdapter extends BaseAdapter<UIConversation> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new ViewHolder(context, parent);
    }

    class ViewHolder extends BaseViewHolder<UIConversation> {

        @BindView(R2.id.ir_iv_avatar)
        ImageView irIvAvatar;
        @BindView(R2.id.ir_iv_group)
        ImageView irIvGroup;
        @BindView(R2.id.ir_tv_name)
        TextView irTvName;

        private ImageLoader imageLoader;

        public ViewHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.im_item_recently);
            imageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
        }

        @Override
        protected void bindData(UIConversation itemValue, int position, OnItemClickListener onItemClickListener) {
            switch (itemValue.getConversationType()) {
                case TEAM:
                    irIvGroup.setVisibility(View.VISIBLE);
                    irIvGroup.setImageResource(R.drawable.im_ic_cv_team);
                    break;
                case SOCIAL:
                    irIvGroup.setVisibility(View.VISIBLE);
                    irIvGroup.setImageResource(R.drawable.im_ic_cv_social);
                    break;
                default:
                    irIvGroup.setVisibility(View.GONE);
                    break;
            }
            irTvName.setText(itemValue.getName());
            imageLoader.loadImage(getContext(), ImageConfigImpl
                    .builder()
                    .placeholder(R.drawable.common_default_circle_placeholder)
                    .errorPic(R.drawable.common_default_circle_placeholder)
                    .isCircle(true)
                    .imageView(irIvAvatar)
                    .url(itemValue.getPortrait())
                    .build());
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }
}