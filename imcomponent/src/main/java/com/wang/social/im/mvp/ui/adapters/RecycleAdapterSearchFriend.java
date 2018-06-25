package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.utils.TimeUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;

import butterknife.BindView;

public class RecycleAdapterSearchFriend extends BaseAdapter<TestEntity> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.lay_userlist);
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
        @BindView(R2.id.text_tags)
        TextView textTags;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(TestEntity bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImgTest(imgHeader);
            textName.setText("阿凡达");
            textLableGender.setSelected(true);
            textLableGender.setText("00后");
            textLableAstro.setText("白羊座");
            textTags.setText("#周杰伦 #旅游");
//            textName.setText("阿凡达");
//            textLableGender.setSelected(!bean.isMale());
//            textLableGender.setText(TimeUtils.getBirthdaySpan(bean.getBirthday()));
//            textLableAstro.setText(TimeUtils.getAstro(bean.getBirthday()));
//            textTag.setText(bean.getTagText());
//            textCount.setText("发布" + bean.getPublishNum() + "条");
        }

        @Override
        public void onRelease() {
        }
    }

}
