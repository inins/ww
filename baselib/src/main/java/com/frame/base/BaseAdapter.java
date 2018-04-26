package com.frame.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter基类, RecyclerView的适配器继承自此类
 * <p/>
 * Created by CJ on 2016/10/27 0027.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 装载每个Item的数据的列表
     */
    protected List<T> valueList = new ArrayList<T>();

    protected OnItemClickListener onItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createViewHolder(parent.getContext(), parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder) holder).setData(valueList.get(position), position, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return valueList == null ? 0 : valueList.size();
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 获取数据集
     *
     * @return
     */
    public List<T> getData() {
        return this.valueList;
    }

    /**
     * 刷新数据
     *
     * @param valueList 新的数据列表
     */
    public void refreshData(List<T> valueList) {
        this.valueList = valueList;
        notifyDataSetChanged();
    }

    /**
     * 刷新数据
     * 使用这种方式可以刷新整个列表的情况下带动画
     */
    public void refreshItem(List<T> items) {
        this.valueList.clear();
        this.valueList.addAll(items);
        notifyItemRangeChanged(0, this.valueList.size());
    }

    /**
     * 移除一项数据
     *
     * @param position 索引
     */
    public void removeItem(int position) {
        this.valueList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.valueList.size() - position);
    }

    /**
     * 插入一项数据
     *
     * @param item 添加的数据
     */
    public void insertItem(int position, T item) {
        this.valueList.add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, this.valueList.size() - position);
    }

    /**
     * 插入数据
     *
     * @param items 添加的数据
     */
    public void insertItem(int position, List<T> items) {
        this.valueList.addAll(position, items);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, this.valueList.size() - position);
    }

    /**
     * 添加一项数据
     *
     * @param item
     */
    public void addItem(T item) {
        this.valueList.add(item);
        notifyItemInserted(this.valueList.size() - 1);
    }

    /**
     * 添加数据
     *
     * @param items
     */
    public void addItem(List<T> items) {
        this.valueList.addAll(items);
        notifyItemInserted(this.valueList.size() - items.size());
        notifyItemRangeChanged(this.valueList.size() - items.size(), this.valueList.size() - 1);
    }

    /**
     * 遍历{@link BaseViewHolder},释放需要释放的资源
     *
     * @param recyclerView
     */
    public static void releaseAllHolder(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--) {
            final View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof BaseViewHolder) {
                ((BaseViewHolder) viewHolder).onRelease();
            }
        }
    }

    /**
     * 创建ViewHolder
     *
     * @param context 上下文
     * @param parent  根视图
     * @return
     */
    protected abstract BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType);

    /**
     * RecyclerView Item点击事件接口
     * <p/>
     * Created by CJ on 2016/10/27 0027.
     */
    public interface OnItemClickListener<T> {
        void onItemClick(T t, int position);
    }
}
