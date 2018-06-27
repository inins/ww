package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.SelectHelper;
import com.frame.utils.StrUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.recommend.RecommendUser;

import java.util.List;

import butterknife.BindView;

public class RecycleAdapterNewguideRecommendFriend extends BaseAdapter<RecommendUser> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.personal_item_newguide_recommend_friend);
    }

    public class Holder extends BaseViewHolder<RecommendUser> {
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
        protected void bindData(RecommendUser bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getAvatar());
            textName.setText(bean.getNickname());
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
        List<RecommendUser> selectBeans = SelectHelper.getSelectBeans(getData());
        if (StrUtil.isEmpty(selectBeans)) return "";
        String ids = "";
        for (RecommendUser user : selectBeans) {
            ids += user.getUserId();
        }
        return StrUtil.subLastChart(ids, ",");
    }

}
