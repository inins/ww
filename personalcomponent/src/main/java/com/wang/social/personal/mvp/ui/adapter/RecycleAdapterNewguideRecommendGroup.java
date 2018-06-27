package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.SelectHelper;
import com.frame.utils.StrUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.recommend.RecommendGroup;
import com.wang.social.personal.mvp.entities.recommend.RecommendUser;

import java.util.List;

import butterknife.BindView;

public class RecycleAdapterNewguideRecommendGroup extends BaseAdapter<RecommendGroup> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.personal_item_newguide_recommend_group);
    }

    public class Holder extends BaseViewHolder<RecommendGroup> {
        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.img_check)
        ImageView imgCheck;
        @BindView(R2.id.text_name)
        TextView textName;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(RecommendGroup bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getHeadUrl());
            textName.setText(bean.getGroupName());
            imgCheck.setSelected(bean.isSelect());

            imgHeader.setOnClickListener(view -> {
                bean.setSelect(!bean.isSelect());
                notifyItemChanged(position);
            });
        }

        @Override
        public void onRelease() {
        }
    }

    public String getSelectIds() {
        List<RecommendGroup> selectBeans = SelectHelper.getSelectBeans(getData());
        if (StrUtil.isEmpty(selectBeans)) return "";
        String ids = "";
        for (RecommendGroup group : selectBeans) {
            ids += group.getId();
        }
        return StrUtil.subLastChart(ids, ",");
    }
}
