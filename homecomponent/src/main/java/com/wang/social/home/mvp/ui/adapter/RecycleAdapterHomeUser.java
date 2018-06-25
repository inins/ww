package com.wang.social.home.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.home.R;
import com.wang.social.home.R2;

import butterknife.BindView;


public class RecycleAdapterHomeUser extends BaseAdapter<TestEntity> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.home_item_user);
    }

    public class Holder extends BaseViewHolder<TestEntity> {
        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.text_name)
        TextView textName;
        @BindView(R2.id.text_tag)
        TextView textTag;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(TestEntity bean, int position, OnItemClickListener onItemClickListener) {
//            ImageLoaderHelper.loadCircleImg(img_header, bean.getImgUrl());
            ImageLoaderHelper.loadCircleImgTest(imgHeader);
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
