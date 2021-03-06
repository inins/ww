package com.wang.social.funshow.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;

import butterknife.BindView;

public class RecycleAdapterTest extends BaseAdapter<TestEntity> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.funshow_item_test);
    }

    public class Holder extends BaseViewHolder<TestEntity> {

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(TestEntity bean, int position, OnItemClickListener onItemClickListener) {
        }

        @Override
        public void onRelease() {
        }
    }
}
