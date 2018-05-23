package com.wang.social.home.mvp.ui.adapter;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.GroupBean;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.utils.StrUtil;
import com.frame.utils.TimeUtils;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.entities.card.CardGroup;
import com.wang.social.home.mvp.entities.card.CardUser;
import com.wang.social.home.mvp.ui.holder.BaseCardViewHolder;

import butterknife.BindView;

public class RecycleAdapterCardGroup extends BaseAdapter<CardGroup> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.home_item_card_group);
    }

    public class Holder extends BaseCardViewHolder<CardGroup> {
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
            setLikeView(imgLike);
            setDisLikeView(imgDislike);
        }

        @Override
        protected void bindData(CardGroup bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadImg(imgPic, bean.getGroupCoverPlan());
            textName.setText(bean.getGroupName());
            textLableCount.setText(bean.getMemberNum() + "人");
            textTag.setText(bean.getTagText());
            textFlag.setVisibility(bean.isFree() ? View.GONE : View.VISIBLE);

            //解析手势，同时识别点击事件和拖拽事件
            GestureDetectorCompat mGestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (onCardGestureListener != null)
                        onCardGestureListener.onItemClick(bean, Holder.this);
                    return false;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    if (onCardGestureListener != null)
                        onCardGestureListener.onItemScroll(bean, Holder.this);
                    return false;
                }
            });

            //设置手势识别器，只给顶层的item添加（顶层item即adapter最后一个）
            itemView.setClickable(true);
            itemView.setOnTouchListener((v, event) -> {
                if (position == getItemCount() - 1) {
                    mGestureDetector.onTouchEvent(event);
                }
                return false;
            });
        }

        @Override
        public void onRelease() {
        }
    }

    ////////////////////////////////

    private OnCardGestureListener onCardGestureListener;

    public void setOnCardGestureListener(OnCardGestureListener onCardGestureListener) {
        this.onCardGestureListener = onCardGestureListener;
    }

    public interface OnCardGestureListener {
        void onItemClick(CardGroup cardGroup, Holder holder);

        void onItemScroll(CardGroup cardGroup, Holder holder);
    }
}
