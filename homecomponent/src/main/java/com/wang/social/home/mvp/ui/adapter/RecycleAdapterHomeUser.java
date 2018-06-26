package com.wang.social.home.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.entities.user.RecommendUser;

import butterknife.BindView;


public class RecycleAdapterHomeUser extends BaseAdapter<RecommendUser> {


    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.home_item_user);
    }

    public class Holder extends BaseViewHolder<RecommendUser> {
        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.text_name)
        TextView textName;
        @BindView(R2.id.text_tag)
        TextView textTag;
        @BindView(R2.id.img_gender)
        ImageView imgGender;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(RecommendUser bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getAvatar());
            textName.setText(bean.getNickname());
            textTag.setText(bean.getTagText());
            imgGender.setSelected(!bean.isMale());
            imgGender.setVisibility(bean.getSex() == -1 ? View.GONE : View.VISIBLE);
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
