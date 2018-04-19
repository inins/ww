package com.wang.social.funshow.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.utils.viewutils.FontUtils;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.ui.view.subevaview.SubEva;
import com.wang.social.funshow.mvp.ui.view.subevaview.SubEvaView;

import java.util.ArrayList;

import butterknife.BindView;

public class RecycleAdapterEva extends BaseAdapter<TestEntity> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.funshow_item_eva);
    }

    public class Holder extends BaseViewHolder<TestEntity> {
        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.text_name)
        TextView text_name;
        @BindView(R2.id.subeva)
        SubEvaView subeva;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(TestEntity itemValue, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImgTest(imgHeader);
            FontUtils.boldText(text_name);

            subeva.refreshData(new ArrayList<SubEva>(){{
                add(new SubEva());
                add(new SubEva());
                add(new SubEva());
            }});
        }

        @Override
        public void onRelease() {
        }
    }
}
