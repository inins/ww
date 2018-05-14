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
import com.wang.social.im.mvp.model.entities.IndexMemberInfo;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-12 17:59
 * ============================================
 */
public class AlertUserSearchAdapter extends BaseAdapter<IndexMemberInfo> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new ViewHolder(context, parent);
    }

    class ViewHolder extends BaseViewHolder<IndexMemberInfo> {

        @BindView(R.id.iau_iv_portrait)
        ImageView iauIvPortrait;
        @BindView(R.id.iau_tv_nickname)
        TextView iauTvNickname;

        ImageLoader imageLoader;

        public ViewHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.im_item_alert_user);
            imageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
        }

        @Override
        protected void bindData(IndexMemberInfo itemValue, int position, OnItemClickListener onItemClickListener) {
            imageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                    .placeholder(R.drawable.common_default_circle_placeholder)
                    .errorPic(R.drawable.common_default_circle_placeholder)
                    .isCircle(true)
                    .imageView(iauIvPortrait)
                    .url(itemValue.getPortrait())
                    .build());

            iauTvNickname.setText(itemValue.getNickname());
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }
}
