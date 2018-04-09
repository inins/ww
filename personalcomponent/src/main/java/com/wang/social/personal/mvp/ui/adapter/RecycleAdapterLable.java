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
import com.wang.social.personal.mvp.entities.lable.Lable;

import java.util.List;

import butterknife.BindView;

public class RecycleAdapterLable extends BaseAdapter<Lable> {

    private boolean deleteEnable;

    public List<Lable> getResults() {
        return valueList;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
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
            img_del.setOnClickListener((view) -> {
                if (onLableDelClickListener != null)
                    onLableDelClickListener.OnDelClick(RecycleAdapterLable.this, lable, position);
            });

            img_del.setVisibility(deleteEnable ? View.VISIBLE : View.GONE);
            img_tag.setVisibility(lable.getShowTagBool() ? View.VISIBLE : View.GONE);
            text_name.setText(lable.getName());
        }

        @Override
        public void onRelease() {
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }

    /////////////////////

    public boolean isDeleteEnable() {
        return deleteEnable;
    }

    public void setDeleteEnable(boolean deleteEnable) {
        this.deleteEnable = deleteEnable;
    }

    private OnLableDelClickListener onLableDelClickListener;

    public void setOnLableDelClickListener(OnLableDelClickListener onLableDelClickListener) {
        this.onLableDelClickListener = onLableDelClickListener;
    }

    public interface OnLableDelClickListener {
        void OnDelClick(RecycleAdapterLable adapter, Lable lable, int position);
    }
}
