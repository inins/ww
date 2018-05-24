package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.OfficialImage;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-24 9:46
 * ============================================
 */
public class BackgroundImageAdapter extends BaseAdapter<OfficialImage> {
    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new ViewHolder(context, parent);
    }

    class ViewHolder extends BaseViewHolder<OfficialImage> {

        private ImageLoader mImageLoader;

        public ViewHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.im_item_background_image);
            mImageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
        }

        @Override
        protected void bindData(OfficialImage itemValue, int position, OnItemClickListener onItemClickListener) {
            mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                    .placeholder(R.drawable.im_image_placeholder)
                    .errorPic(R.drawable.im_image_placeholder)
                    .imageView(((ImageView) itemView))
                    .url(itemValue.getImageUrl())
                    .build());
        }

        @Override
        public void onRelease() {
            mImageLoader.clear(getContext(), ImageConfigImpl.builder()
                    .imageView(((ImageView) itemView))
                    .build());
            super.onRelease();
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }
}
