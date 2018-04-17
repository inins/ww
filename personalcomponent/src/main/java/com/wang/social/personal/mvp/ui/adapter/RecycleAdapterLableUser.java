package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.wang.social.personal.R;
import com.frame.component.entities.TestEntity;

public class RecycleAdapterLableUser extends BaseAdapter<TestEntity> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.personal_item_lable_user);
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

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }
}
