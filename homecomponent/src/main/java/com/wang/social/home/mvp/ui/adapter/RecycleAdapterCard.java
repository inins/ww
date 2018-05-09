package com.wang.social.home.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.entities.Card;

import butterknife.BindView;

public class RecycleAdapterCard extends BaseAdapter<Card> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.home_item_card);
    }

    public class Holder extends BaseViewHolder<Card> {

        @BindView(R2.id.img_pic)
        ImageView imgPic;
        @BindView(R2.id.img_like)
        ImageView imgLike;
        @BindView(R2.id.img_dislike)
        ImageView imgDislike;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Card bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadImgTestByPosition(imgPic, bean.getIndex());
        }

        @Override
        public void onRelease() {
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }

        public void setLikeAlpha(double value) {
            imgLike.setAlpha((float) value);
        }
        public void setDisLikeAlpha(double value) {
            imgDislike.setAlpha((float) value);
        }
    }
}
