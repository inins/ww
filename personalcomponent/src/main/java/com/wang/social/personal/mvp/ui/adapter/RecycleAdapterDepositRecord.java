package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.wang.social.personal.R;
import com.wang.social.personal.interfaces.OnRecycleItemClickListener;
import com.wang.social.personal.mvp.entities.TestEntity;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapterDepositRecord extends BaseAdapter<TestEntity> {

    public List<TestEntity> getResults() {
        return valueList;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent) {
        return new Holder(context, parent, R.layout.personal_item_deposit_record);
    }

    public class Holder extends BaseViewHolder<TestEntity> {
        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(TestEntity itemValue, int position, OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(itemValue, position);
                }
            });
        }

        @Override
        public void onRelease() {
        }
    }
}
