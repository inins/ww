package com.wang.social.funpoint.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.funpoint.R;

import butterknife.BindView;

public class RecycleAdapterHome extends BaseAdapter<TestEntity> {

    ImageLoaderHelper imageLoaderHelper;

    public RecycleAdapterHome(ImageLoaderHelper imageLoaderHelper) {
        this.imageLoaderHelper = imageLoaderHelper;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.funpoint_item_home);
    }

    public class Holder extends BaseViewHolder<TestEntity> {
        @BindView(R.id.img_pic)
        ImageView imgPic;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(TestEntity itemValue, int position, OnItemClickListener onItemClickListener) {
            imageLoaderHelper.loadImgTest(imgPic);
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
