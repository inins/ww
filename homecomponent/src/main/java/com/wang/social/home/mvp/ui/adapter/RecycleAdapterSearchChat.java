package com.wang.social.home.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.home.R;
import com.wang.social.home.R2;

import butterknife.BindView;

public class RecycleAdapterSearchChat extends BaseAdapter<TestEntity> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.home_item_search_user);
    }

    public class Holder extends BaseViewHolder<TestEntity> {
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
        protected void bindData(TestEntity bean, int position, OnItemClickListener onItemClickListener) {
//            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getAvatar());
//            textName.setText(bean.getNickname());
//            textLableGender.setSelected(!bean.isMale());
//            textLableGender.setText(TimeUtils.getBirthdaySpan(bean.getBirthday()));
//            textLableAstro.setText(TimeUtils.getAstro(bean.getBirthday()));
//            textTag.setText(bean.getTagText());
//            textCount.setText("发布" + bean.getPublishNum() + "条");

         ImageLoaderHelper.loadCircleImgTest(imgHeader);
        }

        @Override
        public void onRelease() {
        }
    }
}
