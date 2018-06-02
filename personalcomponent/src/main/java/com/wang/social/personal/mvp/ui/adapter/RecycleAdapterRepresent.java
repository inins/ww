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
import com.wang.social.personal.mvp.entities.user.UserRepresent;

import butterknife.BindView;

public class RecycleAdapterRepresent extends BaseAdapter<UserRepresent> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.personal_item_represent);
    }

    public class Holder extends BaseViewHolder<UserRepresent> {
        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.text_name)
        TextView textName;
        @BindView(R2.id.text_diamond)
        TextView textDiamond;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(UserRepresent bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getAvatar());
            textName.setText(bean.getNickName());
            textDiamond.setText(bean.getGemstone() + "");
        }

        @Override
        public void onRelease() {
        }
    }
}
