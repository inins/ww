package com.wang.social.home.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.utils.StrUtil;
import com.frame.utils.TimeUtils;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.entities.card.CardUser;

import butterknife.BindView;

public class RecycleAdapterCard extends BaseAdapter<CardUser> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.home_item_card);
    }

    public class Holder extends BaseViewHolder<CardUser> {
        @BindView(R2.id.text_name)
        TextView textName;
        @BindView(R2.id.text_lable_gender)
        TextView textLableGender;
        @BindView(R2.id.text_lable_astro)
        TextView textLableAstro;
        @BindView(R2.id.text_tag)
        TextView textTag;
        @BindView(R2.id.text_position)
        TextView textPosition;
        @BindView(R2.id.text_flag)
        TextView textFlag;
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
        protected void bindData(CardUser bean, int position, OnItemClickListener onItemClickListener) {
            if (!StrUtil.isEmpty(bean.getPhotoList())) {
                ImageLoaderHelper.loadImg(imgPic, bean.getPhotoList().get(0).getPhotoUrl());
            } else {
                imgPic.setImageResource(R.drawable.default_rect);
            }
            textName.setText(bean.getNickname());
            textLableGender.setSelected(!bean.isMale());
            textLableGender.setText(TimeUtils.getBirthdaySpan(bean.getBirthday()));
            textLableAstro.setText(TimeUtils.getAstro(bean.getBirthday()));
            textTag.setText(bean.getTagText());
            textPosition.setText(bean.getCity());
            textFlag.setText(bean.getPicCount() + "张图");
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
