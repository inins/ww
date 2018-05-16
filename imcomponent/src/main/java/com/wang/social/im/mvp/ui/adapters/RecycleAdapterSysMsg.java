package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.utils.TimeUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.notify.SysMsg;

import butterknife.BindView;

public class RecycleAdapterSysMsg extends BaseAdapter<SysMsg> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.im_item_sysmsg);
    }

    public class Holder extends BaseViewHolder<SysMsg> {

        @BindView(R2.id.text_title)
        TextView textTitle;
        @BindView(R2.id.text_subtitle)
        TextView textSubtitle;
        @BindView(R2.id.text_content)
        TextView textContent;
        @BindView(R2.id.text_time)
        TextView textTime;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(SysMsg bean, int position, OnItemClickListener onItemClickListener) {
            textTitle.setText(bean.getTitle());
            textSubtitle.setText(bean.getName());
            textContent.setText(bean.getContent());
            textTime.setText(TimeUtils.date2String(bean.getUpdateTime(), "MM-dd HH:mm"));
        }

        @Override
        public void onRelease() {
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }
}
