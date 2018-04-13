package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.utils.StrUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.lable.Lable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecycleAdapterLable extends BaseAdapter<Lable> {

    private boolean deleteEnable;

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.personal_item_lable);
    }

    public class Holder extends BaseViewHolder<Lable> {
        @BindView(R2.id.text_name)
        TextView text_name;
        @BindView(R2.id.img_tag)
        ImageView img_tag;
        @BindView(R2.id.img_del)
        ImageView img_del;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Lable lable, int position, OnItemClickListener onItemClickListener) {
            img_del.setOnClickListener((view) -> {
                if (onLableDelClickListener != null)
                    onLableDelClickListener.OnDelClick(RecycleAdapterLable.this, lable, position);
            });

            img_del.setVisibility(deleteEnable ? View.VISIBLE : View.GONE);
            img_tag.setVisibility(lable.getShowTagBool() ? View.VISIBLE : View.GONE);
            text_name.setText(lable.getName());
        }

        @Override
        public void onRelease() {
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }

    /////////////////////

    public String getIds() {
        return getIds(valueList);
    }

    private String getIds(List<Lable> lableList) {
        if (StrUtil.isEmpty(lableList)) return "";
        String ids = "";
        for (Lable lable : lableList) {
            ids += lable.getId() + ",";
        }
        return StrUtil.subLastChart(ids, ",");
    }

    public String getIdsByAdd(Lable lable) {
        if (StrUtil.isEmpty(valueList)) return String.valueOf(lable.getId());
        List<Lable> tempList = new ArrayList<>();
        tempList.addAll(valueList);
        tempList.add(lable);
        return getIds(tempList);
    }

    public String getIdsByDel(Lable lable) {
        if (StrUtil.isEmpty(valueList)) return "";
        List<Lable> tempList = new ArrayList<>();
        for (Lable item : valueList) {
            if (item.getId() == lable.getId()) continue;
            tempList.add(item);
        }
        return getIds(tempList);
    }

    /////////////////////

    public boolean isDeleteEnable() {
        return deleteEnable;
    }

    public void setDeleteEnable(boolean deleteEnable) {
        this.deleteEnable = deleteEnable;
    }

    private OnLableDelClickListener onLableDelClickListener;

    public void setOnLableDelClickListener(OnLableDelClickListener onLableDelClickListener) {
        this.onLableDelClickListener = onLableDelClickListener;
    }

    public interface OnLableDelClickListener {
        void OnDelClick(RecycleAdapterLable adapter, Lable lable, int position);
    }
}
