package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.utils.TimeUtils;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.deposit.DepositRecord;

import butterknife.BindView;

public class RecycleAdapterDepositRecord extends BaseAdapter<DepositRecord> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.personal_item_deposit_record);
    }

    public class Holder extends BaseViewHolder<DepositRecord> {
        @BindView(R2.id.text_time)
        TextView textTime;
        @BindView(R2.id.text_detail)
        TextView textDetail;
        @BindView(R2.id.text_status)
        TextView textStatus;
        @BindView(R2.id.text_amount)
        TextView textAmount;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(DepositRecord bean, int position, OnItemClickListener onItemClickListener) {
            textTime.setText(TimeUtils.date2String(bean.getCreateTime(), "yyyy.MM.dd HH:mm"));
            textAmount.setText(bean.getPrice() + "元");
            textDetail.setText("支付宝：" + bean.getAlipayLogonid());
            textStatus.setSelected(!bean.isSuccess());
            textStatus.setText(bean.getStatusText());
        }

        @Override
        public void onRelease() {
        }
    }
}
