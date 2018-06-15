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
import com.wang.social.im.mvp.model.entities.SimpleSocial;

import butterknife.BindView;

/**
 * ============================================
 * 创建觅聊时趣聊选择列表适配器
 * <p>
 * Create by ChenJing on 2018-06-14 10:30
 * ============================================
 */
public class CTSocialAdapter extends BaseAdapter<SimpleSocial> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new ViewHolder(context, parent);
    }

    class ViewHolder extends BaseViewHolder<SimpleSocial> {

        @BindView(R2.id.cts_iv_avatar)
        ImageView ctsIvAvatar;
        @BindView(R2.id.cts_tv_name)
        TextView ctsTvName;

        ImageLoader imageLoader;

        public ViewHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.im_item_create_team_social);
            imageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
        }

        @Override
        protected void bindData(SimpleSocial itemValue, int position, OnItemClickListener onItemClickListener) {
            imageLoader.loadImage(getContext(), ImageConfigImpl
                    .builder()
                    .placeholder(R.drawable.common_default_circle_placeholder)
                    .errorPic(R.drawable.common_default_circle_placeholder)
                    .isCircle(true)
                    .url(itemValue.getAvatar())
                    .imageView(ctsIvAvatar)
                    .build());

            ctsTvName.setText(itemValue.getName());
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }
}
