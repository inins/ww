package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.wang.social.im.mvp.model.entities.EnvelopAdoptInfo;
import com.wang.social.im.mvp.model.entities.EnvelopInfo;
import com.wang.social.im.mvp.ui.adapters.holders.EnvelopAdoptHeaderViewHolder;
import com.wang.social.im.mvp.ui.adapters.holders.EnvelopAdoptViewHolder;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-24 14:29
 * ============================================
 */
public class EnvelopAdoptInfoAdapter extends BaseAdapter<Object> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new EnvelopAdoptHeaderViewHolder(context, parent);
            default:
                return new EnvelopAdoptViewHolder(context, parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((EnvelopAdoptHeaderViewHolder) holder).setData((EnvelopInfo) valueList.get(position), position, onItemClickListener);
                break;
            case TYPE_ITEM:
                ((EnvelopAdoptViewHolder) holder).setData((EnvelopAdoptInfo) valueList.get(position), position, onItemClickListener);
                break;
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (valueList.get(position) instanceof EnvelopInfo) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }
}
