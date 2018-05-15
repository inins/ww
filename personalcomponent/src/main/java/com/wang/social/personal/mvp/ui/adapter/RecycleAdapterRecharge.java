package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.recharge.Recharge;

import butterknife.BindView;

public class RecycleAdapterRecharge extends BaseAdapter<Recharge> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.personal_item_recharge);
    }

    public class Holder extends BaseViewHolder<Recharge> {

        @BindView(R2.id.text_title)
        TextView textTitle;
        @BindView(R2.id.text_price)
        TextView textPrice;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Recharge bean, int position, OnItemClickListener onItemClickListener) {
            textPrice.setText("¥ " + (int)bean.getPrice());
            textTitle.setText(bean.getPackageName());
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
