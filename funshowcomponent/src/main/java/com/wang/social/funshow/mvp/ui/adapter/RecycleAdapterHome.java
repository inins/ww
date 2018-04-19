package com.wang.social.funshow.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.ui.dialog.MorePopupWindow;

import butterknife.BindView;

public class RecycleAdapterHome extends BaseAdapter<TestEntity> {


    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.funshow_item_home);
    }

    public class Holder extends BaseViewHolder<TestEntity> {
        @BindView(R2.id.img_header)
        ImageView img_header;
        @BindView(R2.id.img_pic)
        ImageView imgPic;
        @BindView(R2.id.img_more)
        ImageView imgMore;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(TestEntity itemValue, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImgTest(img_header);
            ImageLoaderHelper.loadImgTest(imgPic);
            imgMore.setOnClickListener(view -> {
                new MorePopupWindow(getContext()).showPopupWindow(view);
            });
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
