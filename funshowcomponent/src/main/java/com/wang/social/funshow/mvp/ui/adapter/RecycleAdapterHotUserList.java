package com.wang.social.funshow.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.utils.TimeUtils;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.user.TopUser;

import butterknife.BindView;

public class RecycleAdapterHotUserList extends BaseAdapter<TopUser> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.funshow_item_hotuser);
    }

    public class Holder extends BaseViewHolder<TopUser> {
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
        @BindView(R2.id.text_count)
        TextView textCount;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(TopUser bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getAvatar());
            textName.setText(bean.getNickname());
            textLableGender.setSelected(!bean.isMale());
            textLableGender.setText(TimeUtils.getBirthdaySpan(bean.getBirthday()));
            textLableAstro.setText(TimeUtils.getAstro(bean.getBirthday()));
            textTag.setText(bean.getTagText());
            textCount.setText("发布" + bean.getPublishNum() + "条");
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
