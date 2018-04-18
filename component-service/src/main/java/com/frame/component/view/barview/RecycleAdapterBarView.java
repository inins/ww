package com.frame.component.view.barview;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.service.R;
import com.frame.utils.FrameUtils;


public class RecycleAdapterBarView extends BaseAdapter<BarUser> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.item_view_bar);
    }

    public class Holder extends BaseViewHolder<BarUser> {
        ImageView img_header;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
            img_header = itemView.findViewById(R.id.img_header);
        }

        @Override
        protected void bindData(BarUser bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(img_header, bean.getImgUrl());
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
