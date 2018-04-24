package com.wang.social.funshow.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.utils.viewutils.FontUtils;
import com.frame.mvp.IView;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.eva.Comment;
import com.wang.social.funshow.mvp.ui.view.subevaview.SubEvaView;
import com.wang.social.funshow.net.helper.NetZanHelper;
import com.wang.social.funshow.utils.FunShowUtil;

import java.util.ArrayList;

import butterknife.BindView;

public class RecycleAdapterEva extends BaseAdapter<Comment> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.funshow_item_eva);
    }

    public class Holder extends BaseViewHolder<Comment> {
        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.text_name)
        TextView text_name;
        @BindView(R2.id.text_time)
        TextView textTime;
        @BindView(R2.id.text_zan)
        TextView textZan;
        @BindView(R2.id.text_eva)
        TextView textEva;
        @BindView(R2.id.text_reply_more)
        TextView textReplyMore;
        @BindView(R2.id.subeva)
        SubEvaView subeva;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Comment bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getAvatar());
            text_name.setText(bean.getNickname());
            textEva.setText(bean.getContent());
            textTime.setText(FunShowUtil.getFunshowTimeStr(bean.getCreateTime()));
            textZan.setText(String.valueOf(bean.getSupportTotal()));
            FontUtils.boldText(text_name);
            textReplyMore.setVisibility(bean.getCommentReplySize() > subeva.getMaxSize() ? View.VISIBLE : View.GONE);
            subeva.refreshData(bean.getCommentReply());
            textReplyMore.setText(subeva.isShowAll() ? R.string.funshow_home_funshow_detail_eva_more : R.string.funshow_home_funshow_detail_eva_less);

            textReplyMore.setOnClickListener(v -> {
                subeva.setShowAll(!subeva.isShowAll());
                notifyItemChanged(position);
            });
            textZan.setOnClickListener(v -> {
                IView iView = (getContext() instanceof IView) ? (IView) getContext() : null;
                NetZanHelper.newInstance().funshowCommentZan(iView, textZan, 1, bean.getCommentId(), !textZan.isSelected(), (isZan, zanCount) -> {
                    bean.setSupportTotal(zanCount);
                });
            });
        }

        @Override
        public void onRelease() {
        }
    }
}
