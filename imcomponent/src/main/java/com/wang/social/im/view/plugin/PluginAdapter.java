package com.wang.social.im.view.plugin;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.frame.base.BaseAdapter;
import com.frame.utils.SizeUtils;
import com.tencent.imsdk.TIMConversationType;
import com.wang.social.im.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bo on 2018-03-29.
 */

public class PluginAdapter {

    private static final int COLUMN = 4;
    private static final int PAGE_ITEM_COUNT = COLUMN * 2;

    private LayoutInflater mLayoutInflater;

    private ViewPager mPluginsPager;

    private List<PluginModule> plugins;
    private boolean isInitialed;

    private OnPluginClickListener pluginClickListener;

    public void setPluginClickListener(OnPluginClickListener pluginClickListener) {
        this.pluginClickListener = pluginClickListener;
    }

    public void setPlugins(List<PluginModule> plugins) {
        this.plugins = plugins;
    }

    public boolean isInitialed(){
        return isInitialed;
    }

    public int getVisibility(){
        return mPluginsPager == null ? View.GONE : mPluginsPager.getVisibility();
    }

    public void setVisibility(int visibility){
        if (mPluginsPager != null){
            mPluginsPager.setVisibility(visibility);
        }
    }

    public void bindView(ViewGroup viewGroup) {
        isInitialed = true;
        initView(viewGroup.getContext(), viewGroup);
    }

    private void initView(Context context, ViewGroup viewGroup) {
        mLayoutInflater = LayoutInflater.from(context);
        mPluginsPager = (ViewPager) mLayoutInflater.inflate(R.layout.im_input_plugins, null);
        int height = context.getResources().getDimensionPixelSize(R.dimen.im_chat_input_plugin_board_height);
        mPluginsPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        viewGroup.addView(mPluginsPager);
        int pages = 0;
        int count = plugins.size();
        if (count > 0){
            int tmp = count % PAGE_ITEM_COUNT;
            if (tmp > 0){
                tmp = 1;
            }
            pages = count / PAGE_ITEM_COUNT + tmp;
        }
        mPluginsPager.setAdapter(new PluginPagerAdapter(pages));
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mLayoutInflater = null;
        mPluginsPager = null;
        plugins = null;
        pluginClickListener = null;
    }

    private class PluginPagerAdapter extends PagerAdapter {

        int pages;

        public PluginPagerAdapter(int pages) {
            this.pages = pages;
        }

        @Override
        public int getCount() {
            return pages;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            RecyclerView pluginContainer = (RecyclerView) mLayoutInflater.inflate(R.layout.im_input_plugin_grid, null);
            pluginContainer.setLayoutManager(new GridLayoutManager(container.getContext(), COLUMN));
            final int space = SizeUtils.dp2px(10);
            pluginContainer.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.left = space;
                    outRect.bottom = space;
                    if (parent.getChildLayoutPosition(view) < COLUMN) {
                        outRect.top = space;
                    }
                    if((parent.getChildLayoutPosition(view) + 1) % COLUMN == 0 ){
                        outRect.right = space;
                    }
                }
            });
            int showSize = (position + 1) * PAGE_ITEM_COUNT;
            List<PluginModule> pluginModules;
            if (showSize > plugins.size()) {
                if (position == 0) {
                    pluginModules = plugins;
                } else {
                    pluginModules = plugins.subList(position * PAGE_ITEM_COUNT, plugins.size() - 1);
                }
            } else {
                pluginModules = plugins.subList(position * PAGE_ITEM_COUNT, showSize);
            }
            PluginGridAdapter pluginGridAdapter = new PluginGridAdapter(pluginModules);
            pluginContainer.setAdapter(pluginGridAdapter);
            pluginGridAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<PluginModule>() {
                @Override
                public void onItemClick(PluginModule pluginModule, int position) {
                    pluginClickListener.onPluginClick(pluginModule);
                }
            });
            container.addView(pluginContainer);
            return pluginContainer;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            View layout = (View)object;
            container.removeView(layout);
        }
    }

    public interface OnPluginClickListener {

        void onPluginClick(PluginModule pluginModule);
    }
}
