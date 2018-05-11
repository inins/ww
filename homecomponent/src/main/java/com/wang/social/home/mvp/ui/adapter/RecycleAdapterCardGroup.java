package com.wang.social.home.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.utils.StrUtil;
import com.frame.utils.TimeUtils;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.entities.card.CardGroup;
import com.wang.social.home.mvp.entities.card.CardUser;

import butterknife.BindView;

public class RecycleAdapterCardGroup extends BaseAdapter<CardGroup> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.home_item_card_group);
    }

    public class Holder extends BaseViewHolder<CardGroup> {
        @BindView(R2.id.text_name)
        public TextView textName;
        @BindView(R2.id.text_lable_count)
        public TextView textLableCount;
        @BindView(R2.id.text_tag)
        public TextView textTag;
        @BindView(R2.id.text_flag)
        ImageView textFlag;
        @BindView(R2.id.img_pic)
        public ImageView imgPic;
        @BindView(R2.id.img_like)
        ImageView imgLike;
        @BindView(R2.id.img_dislike)
        ImageView imgDislike;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(CardGroup bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadImg(imgPic, bean.getGroupCoverPlan());
            textName.setText(bean.getGroupName());
            textLableCount.setText(bean.getMemberNum() + "人");
            textTag.setText(bean.getTagText());
            textFlag.setVisibility(bean.isFree() ? View.GONE : View.VISIBLE);

            itemView.setOnClickListener(v -> {
                if (onCardClickListener != null)
                    onCardClickListener.onItemClick(bean, position, this);
            });
        }

        @Override
        public void onRelease() {
        }

        public void setLikeAlpha(double value) {
            imgLike.setAlpha((float) value);
        }

        public void setDisLikeAlpha(double value) {
            imgDislike.setAlpha((float) value);
        }
    }

    ////////////////////////////////

    private OnCardClickListener onCardClickListener;

    public void setOnCardClickListener(OnCardClickListener onCardClickListener) {
        this.onCardClickListener = onCardClickListener;
    }

    public interface OnCardClickListener {
        void onItemClick(CardGroup cardUser, int position, Holder holder);
    }
}
