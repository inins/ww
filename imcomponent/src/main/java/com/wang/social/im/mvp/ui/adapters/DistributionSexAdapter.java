package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.component.view.CircleProgress;
import com.frame.utils.SizeUtils;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.DistributionSex;

import java.util.List;

public class DistributionSexAdapter extends RecyclerView.Adapter<DistributionSexAdapter.ViewHolder> {

    private Context mContext;
    private List<DistributionSex> mList;
    private int count;

    public DistributionSexAdapter(Context context, List<DistributionSex> list) {
        mContext = context.getApplicationContext();
        mList = list;

        resetCount(list);
    }

    public void resetCount(List<DistributionSex> list) {
        if (null != list) {
            for (DistributionSex age : list) {
                count += age.getCount();
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.im_adapter_distribution_age, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mList) return;
        if (position < 0 || position >= mList.size()) return;

        DistributionSex sex = mList.get(position);

        if (null == sex) return;

        holder.cp.setTextColor(mContext.getResources().getColor(R.color.common_blue_deep));
        holder.cp.setTextSize(SizeUtils.sp2px(14));
        holder.cp.setStartColor(mContext.getResources().getColor(R.color.common_blue_light));
        holder.cp.setEndColor(mContext.getResources().getColor(R.color.common_blue_deep));
        if (count > 0) {
            holder.cp.setProgress(sex.getCount() * 100 / count);
        }
        String gender = "其他";
        if (sex.getSex() == 0) {
            gender = "男生";
        } else if (sex.getSex() == 1) {
            gender = "女生";
        }
        holder.tv.setText(String.format("%1$s-%2$d人", gender, sex.getCount()));
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleProgress cp;
        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);

            cp = itemView.findViewById(R.id.circle_progress);
            tv = itemView.findViewById(R.id.text_view);
        }
    }
}
