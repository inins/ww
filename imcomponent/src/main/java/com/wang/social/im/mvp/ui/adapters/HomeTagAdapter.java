package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.ui.acticity.tags.Tag;
import com.wang.social.im.R;

import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-07 11:25
 * ============================================
 */
public class HomeTagAdapter extends BaseAdapter<Tag> {

    public HomeTagAdapter(List<Tag> tags) {
        this.valueList = tags;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new ViewHolder(context, parent);
    }

    private class ViewHolder extends BaseViewHolder<Tag> {

        public ViewHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.im_item_home_tag);
        }

        @Override
        protected void bindData(Tag itemValue, int position, OnItemClickListener onItemClickListener) {
            ((TextView) itemView).setText(itemValue.getTagName());
        }
    }
}
