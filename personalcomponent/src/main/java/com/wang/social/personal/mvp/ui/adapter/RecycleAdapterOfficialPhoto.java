package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.TestEntity;
import com.wang.social.personal.mvp.entities.photo.OffiPic;
import com.wang.social.personal.mvp.entities.photo.Photo;

import butterknife.BindView;

public class RecycleAdapterOfficialPhoto extends BaseAdapter<OffiPic> {

    private ImageLoader mImageLoader;

    public RecycleAdapterOfficialPhoto(ImageLoader mImageLoader) {
        this.mImageLoader = mImageLoader;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new RecycleAdapterOfficialPhoto.Holder(context, parent, R.layout.personal_item_officialphoto);
    }

    public class Holder extends BaseViewHolder<OffiPic> {

        @BindView(R2.id.img_photo)
        ImageView img_photo;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(OffiPic bean, int position, OnItemClickListener onItemClickListener) {
            mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                    .imageView(img_photo)
                    .url(bean.getPicUrl())
                    .build());
        }

        @Override
        public void onRelease() {
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }
}
