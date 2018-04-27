package com.wang.social.funpoint.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.funpoint.R;
import com.wang.social.funpoint.R2;
import com.wang.social.funpoint.mvp.entities.Funpoint;
import com.wang.social.funpoint.utils.FunPointUtil;

import butterknife.BindView;

public class RecycleAdapterHome extends BaseAdapter<Funpoint> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.funpoint_item_home);
    }

    public class Holder extends BaseViewHolder<Funpoint> {
        @BindView(R2.id.text_title)
        TextView textTitle;
        @BindView(R2.id.text_note)
        TextView textNote;
        @BindView(R2.id.img_pic)
        ImageView imgPic;
        @BindView(R2.id.card_pic)
        CardView cardPic;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Funpoint bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadImg(imgPic, bean.getImgUrl());
            textTitle.setText(bean.getNewsTitle());
            cardPic.setVisibility(!TextUtils.isEmpty(bean.getImgUrl()) ? View.VISIBLE : View.GONE);
            textNote.setText(bean.getNoteStr());
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
