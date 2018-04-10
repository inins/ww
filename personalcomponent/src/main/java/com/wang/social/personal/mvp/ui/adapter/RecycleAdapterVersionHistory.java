package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.TestEntity;
import com.wang.social.personal.mvp.entities.version.VersionHistory;

import java.util.List;

import butterknife.BindView;

public class RecycleAdapterVersionHistory extends BaseAdapter<VersionHistory> {

    public List<VersionHistory> getResults() {
        return valueList;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.personal_item_version_history);
    }

    public class Holder extends BaseViewHolder<VersionHistory> {
        @BindView(R2.id.text_version)
        TextView textVersion;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(VersionHistory bean, int position, OnItemClickListener onItemClickListener) {
            textVersion.setText(bean.getVersionText());
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }

        @Override
        public void onRelease() {
        }
    }
}
