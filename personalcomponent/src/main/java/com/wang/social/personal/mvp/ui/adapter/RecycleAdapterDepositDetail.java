package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.utils.viewutils.FontUtils;
import com.frame.utils.TimeUtils;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.income.DiamondStoneIncome;

import butterknife.BindView;

public class RecycleAdapterDepositDetail extends BaseAdapter<DiamondStoneIncome> {

    private boolean isDiamond;

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.personal_item_deposit_detail);
    }

    public class Holder extends BaseViewHolder<DiamondStoneIncome> {

        @BindView(R2.id.text_name)
        TextView textName;
        @BindView(R2.id.text_time)
        TextView textTime;
        @BindView(R2.id.text_amount)
        TextView textAmount;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(DiamondStoneIncome bean, int position, OnItemClickListener onItemClickListener) {
            textName.setText(bean.getType() + " " + bean.getContent());
            textTime.setText(TimeUtils.date2String(bean.getDateTime(), "yyyy.MM.dd HH:mm"));
            FontUtils.boldText(textAmount);

            float amount;
            if (isDiamond) {
                amount = bean.getAmountDiamond();
                textAmount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.personal_ic_diamond_small, 0);
            } else {
                amount = bean.getGemstone();
                textAmount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.personal_ic_stone_small, 0);
            }

            String amountStr = getAmountShowText(amount);
            textAmount.setText(amountStr);
            if (amount > 0) {
                textAmount.setTextColor(ContextCompat.getColor(getContext(), R.color.common_blue_deep));
            } else {
                textAmount.setTextColor(ContextCompat.getColor(getContext(), R.color.common_text_blank));
            }
        }

        @Override
        public void onRelease() {
        }
    }

    private String getAmountShowText(float ammount) {
        if (ammount != 0) {
            return (ammount > 0 ? "+" : "-") + Math.abs(ammount);
        } else {
            return ammount + "";
        }
    }

    //////////////////


    public boolean isDiamond() {
        return isDiamond;
    }

    public void setDiamond(boolean diamond) {
        isDiamond = diamond;
    }
}
