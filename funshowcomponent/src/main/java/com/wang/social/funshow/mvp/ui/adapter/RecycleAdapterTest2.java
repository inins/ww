package com.wang.social.funshow.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.component.entities.TestEntity;
import com.frame.component.helper.SelectHelper;
import com.wang.social.funshow.R;

import java.util.ArrayList;
import java.util.List;


public class RecycleAdapterTest2 extends RecyclerView.Adapter<RecycleAdapterTest2.Holder> {

    private List<TestEntity> results = new ArrayList<>();

    public List<TestEntity> getData() {
        return results;
    }

    public RecycleAdapterTest2() {
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.funshow_item_test, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
    }

    public class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

}
