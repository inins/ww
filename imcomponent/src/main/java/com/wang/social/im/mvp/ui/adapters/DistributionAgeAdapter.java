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
import com.wang.social.im.mvp.model.entities.DistributionAge;

import java.util.List;

public class DistributionAgeAdapter extends RecyclerView.Adapter<DistributionAgeAdapter.ViewHolder> {

    private Context mContext;
    private List<DistributionAge> mList;
    private int count;

    public DistributionAgeAdapter(Context context, List<DistributionAge> list) {
        mContext = context.getApplicationContext();
        mList = list;

        resetCount(list);
    }

    public void resetCount(List<DistributionAge> list) {
        if (null != list) {
            for (DistributionAge age : list) {
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

        DistributionAge age = mList.get(position);

        if (null == age) return;

        holder.cp.setTextColor(mContext.getResources().getColor(R.color.common_blue_deep));
        holder.cp.setTextSize(SizeUtils.sp2px(14));
        holder.cp.setStartColor(mContext.getResources().getColor(R.color.common_blue_light));
        holder.cp.setEndColor(mContext.getResources().getColor(R.color.common_blue_deep));
        if (count > 0) {
            holder.cp.setProgress(age.getCount() * 100 / count);
        }
        String ageTag = age.getAgeRange().equals("other") ? "" : age.getAgeRange();
        holder.tv.setText("" + ageTag + "后-" + age.getCount() + "人");
//        holder.tv.setText(String.format("%1s后-%2d人", age.getAgeRange(), age.getCount()));
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
