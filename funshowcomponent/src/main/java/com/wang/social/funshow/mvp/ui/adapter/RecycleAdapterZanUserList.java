package com.wang.social.funshow.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.utils.TimeUtils;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.user.ZanUser;

import butterknife.BindView;

public class RecycleAdapterZanUserList extends BaseAdapter<ZanUser> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.funshow_item_zanuser);
    }

    public class Holder extends BaseViewHolder<ZanUser> {
        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.text_lable_gender)
        TextView textLableGender;
        @BindView(R2.id.text_lable_astro)
        TextView textLableAstro;
        @BindView(R2.id.text_name)
        TextView textName;
        @BindView(R2.id.text_tag)
        TextView textTag;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(ZanUser bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getAvatar());
            textName.setText(bean.getNickname());
            textLableGender.setSelected(!bean.isMale());
            textLableGender.setText(TimeUtils.getBirthdaySpan(bean.getBirthday()));
            textLableAstro.setText(TimeUtils.getAstro(bean.getBirthday()));
            textTag.setText(bean.getTags());
        }

        @Override
        public void onRelease() {
        }
    }
}
