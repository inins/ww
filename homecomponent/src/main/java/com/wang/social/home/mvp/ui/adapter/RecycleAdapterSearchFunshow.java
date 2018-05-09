package com.wang.social.home.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.home.R;
import com.wang.social.home.R2;

import butterknife.BindView;

public class RecycleAdapterSearchFunshow extends BaseAdapter<TestEntity> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.home_lay_funshow_item);
    }

    public class Holder extends BaseViewHolder<TestEntity> {

        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.img_pic)
        ImageView imgPic;

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

         ImageLoaderHelper.loadImgTest(imgPic);
         ImageLoaderHelper.loadCircleImgTest(imgHeader);
        }

        @Override
        public void onRelease() {
        }
    }
}
