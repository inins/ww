package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.lable.MiChat;

import butterknife.BindView;

public class RecycleAdapterLableUser extends BaseAdapter<MiChat> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.personal_item_lable_user);
    }

    public class Holder extends BaseViewHolder<MiChat> {

        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.text_name)
        TextView textName;
        @BindView(R2.id.text_detail)
        TextView textDetail;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(MiChat bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getAvatar());
            textName.setText(bean.getNickname());
            textDetail.setText(bean.getMemberNum() + "位成员");
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
