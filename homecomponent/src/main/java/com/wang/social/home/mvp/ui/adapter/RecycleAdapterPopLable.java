package com.wang.social.home.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.SelectHelper;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.entities.Lable;

import butterknife.BindView;

public class RecycleAdapterPopLable extends BaseAdapter<Lable> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.home_item_pop_card);
    }

    public class Holder extends BaseViewHolder<Lable> {

        @BindView(R2.id.text_name)
        TextView textName;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Lable lable, int position, OnItemClickListener onItemClickListener) {
            textName.setText(lable.getName());
            itemView.setOnClickListener(v -> {
                boolean select = !lable.isSelect();
                SelectHelper.selectAllSelectBeans(valueList, false);
                lable.setSelect(select);
                notifyDataSetChanged();
                if (onLableSelectListener != null && select) {
                    onLableSelectListener.onSelect(itemView, lable, position);
                }
            });

            textName.setSelected(lable.isSelect());
        }

        @Override
        public void onRelease() {
        }
    }

    /////////////////////////////////

    private OnLableSelectListener onLableSelectListener;

    public void setOnLableSelectListener(OnLableSelectListener onLableSelectListener) {
        this.onLableSelectListener = onLableSelectListener;
    }

    public interface OnLableSelectListener {
        void onSelect(View view, Lable lable, int position);
    }
}
