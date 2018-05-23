package com.wang.social.im.mvp.ui.adapters;

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
import com.frame.utils.TimeUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.notify.CommonMsg;

import butterknife.BindView;

public class RecycleAdapterCommonMsg extends BaseAdapter<CommonMsg> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.im_item_commonmsg);
    }

    public class Holder extends BaseViewHolder<CommonMsg> {
        @BindView(R2.id.img_dot)
        ImageView imgDot;
        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.text_time)
        TextView textTime;
        @BindView(R2.id.text_name)
        TextView textName;
        @BindView(R2.id.text_content)
        TextView textContent;
        @BindView(R2.id.img_pic)
        ImageView imgPic;
        @BindView(R2.id.card_img)
        CardView cardImg;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(CommonMsg bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getAvatar());
            ImageLoaderHelper.loadImg(imgPic, bean.getPicUrl());
            textName.setText(bean.getName());
            textTime.setText(TimeUtils.date2String(bean.getTime(), "MM-dd HH:mm"));
            textContent.setText(bean.getContent());
            cardImg.setVisibility(TextUtils.isEmpty(bean.getPicUrl()) ? View.GONE : View.VISIBLE);
            imgDot.setVisibility(bean.isRead() ? View.GONE : View.VISIBLE);
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
