package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetZanHelper;
import com.frame.component.utils.FunShowUtil;
import com.frame.component.view.FunshowView;
import com.frame.mvp.IView;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.funshow.FunshowMe;

import butterknife.BindView;

public class RecycleAdapterMeFunshow extends BaseAdapter<FunshowMe> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.lay_item_funshow);
    }

    public class Holder extends BaseViewHolder<FunshowMe> {

        @BindView(R2.id.funshow_view)
        FunshowView funshowView;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(FunshowMe bean, int position, OnItemClickListener onItemClickListener) {
            funshowView.setData(bean.tans2FunshowBean());
        }

        @Override
        public void onRelease() {
        }
    }
}
