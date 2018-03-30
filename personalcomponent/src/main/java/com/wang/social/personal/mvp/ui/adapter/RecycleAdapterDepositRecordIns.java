package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wang.social.personal.R;
import com.wang.social.personal.interfaces.OnRecycleItemClickListener;
import com.wang.social.personal.mvp.entities.TestEntity;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapterDepositRecordIns extends RecyclerView.Adapter<RecycleAdapterDepositRecordIns.Holder> {

    private List<TestEntity> results = new ArrayList<>();

    public List<TestEntity> getResults() {
        return results;
    }

    public RecycleAdapterDepositRecordIns() {
    }

    @Override
    public RecycleAdapterDepositRecordIns.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_item_deposit_record, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterDepositRecordIns.Holder holder, final int position) {
        final TestEntity product = results.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(holder, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }

    private OnRecycleItemClickListener listener;

    public void setOnItemClickListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }
}
