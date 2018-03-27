package com.frame.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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
    protected List<T> valueList;

    private OnItemClickListener onItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createViewHolder(parent.getContext(), parent);
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
     * 刷新数据
     *
     * @param valueList 新的数据列表
     */
    public void refreshData(List<T> valueList) {
        this.valueList = valueList;
        notifyDataSetChanged();
    }

    /**
     * 移除一项数据
     * @param position 索引
     */
    public void removeItem(int position, List<T> valueList){
        this.valueList = valueList;
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.valueList.size() - position);
    }

    /**
     * 添加一项数据
     * @param valueList 添加的数据
     */
    public void insertItem(int position, List<T> valueList){
        this.valueList = valueList;
        notifyItemInserted(position);
        notifyItemRangeChanged(position, this.valueList.size() - position);
    }

    /**
     * 遍历{@link BaseViewHolder},释放需要释放的资源
     * @param recyclerView
     */
    public static void releaseAllHolder(RecyclerView recyclerView){
        if (recyclerView == null){
            return;
        }
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--){
            final View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof BaseViewHolder){
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
    protected abstract BaseViewHolder createViewHolder(Context context, ViewGroup parent);

    /**
     * RecyclerView Item点击事件接口
     * <p/>
     * Created by CJ on 2016/10/27 0027.
     */
    public interface OnItemClickListener<T> {
        void onItemClick(T t, int position);
    }
}
