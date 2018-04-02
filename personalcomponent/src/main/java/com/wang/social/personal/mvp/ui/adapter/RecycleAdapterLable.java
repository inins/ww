package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.Lable;
import com.wang.social.personal.mvp.entities.TestEntity;

import java.util.List;

import butterknife.BindView;

public class RecycleAdapterLable extends BaseAdapter<Lable> {

    private boolean deleteEnable;

    public List<Lable> getResults() {
        return valueList;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent) {
        return new Holder(context, parent, R.layout.personal_item_lable);
    }

    public class Holder extends BaseViewHolder<Lable> {
        @BindView(R2.id.text_name)
        TextView text_name;
        @BindView(R2.id.img_tag)
        ImageView img_tag;
        @BindView(R2.id.img_del)
        ImageView img_del;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Lable lable, int position, OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(lable, position);
                }
            });

            img_del.setVisibility(deleteEnable ? View.VISIBLE : View.GONE);
            img_tag.setVisibility(lable.isShowTag() ? View.VISIBLE : View.GONE);
            text_name.setText(lable.getName());
        }

        @Override
        public void onRelease() {
        }
    }

    /////////////////////

    public boolean isDeleteEnable() {
        return deleteEnable;
    }

    public void setDeleteEnable(boolean deleteEnable) {
        this.deleteEnable = deleteEnable;
    }
}