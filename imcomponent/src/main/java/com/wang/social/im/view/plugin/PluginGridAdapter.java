package com.wang.social.im.view.plugin;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.wang.social.im.R;
import com.wang.social.im.R2;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Bo on 2018-03-29.
 */

public class PluginGridAdapter extends BaseAdapter<PluginModule> {

    public PluginGridAdapter(List<PluginModule> plugins) {
        this.valueList = plugins;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new ViewHolder(context, parent);
    }

    class ViewHolder extends BaseViewHolder<PluginModule> {

        @BindView(R2.id.ipi_plugin_icon)
        ImageView ipiPluginIcon;
        @BindView(R2.id.ipi_plugin_name)
        TextView ipiPluginName;

        public ViewHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.im_input_plugin_item);
        }

        @Override
        protected void bindData(PluginModule itemValue, int position, OnItemClickListener onItemClickListener) {
            ipiPluginIcon.setImageResource(itemValue.getDrawableResId());
            ipiPluginName.setText(itemValue.getPluginName());
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }

        @Override
        public void onRelease() {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }
}
