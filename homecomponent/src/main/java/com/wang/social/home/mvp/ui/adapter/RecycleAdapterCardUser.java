package com.wang.social.home.mvp.ui.adapter;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.utils.StrUtil;
import com.frame.utils.TimeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.entities.card.CardUser;
import com.wang.social.home.mvp.ui.holder.BaseCardViewHolder;

import butterknife.BindView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class RecycleAdapterCardUser extends BaseAdapter<CardUser> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.home_item_card_user);
    }

    public class Holder extends BaseCardViewHolder<CardUser> {
        @BindView(R2.id.text_name)
        public TextView textName;
        @BindView(R2.id.text_lable_gender)
        public TextView textLableGender;
        @BindView(R2.id.text_lable_astro)
        public TextView textLableAstro;
        @BindView(R2.id.text_tag)
        public TextView textTag;
        @BindView(R2.id.text_position)
        public TextView textPosition;
        @BindView(R2.id.text_flag)
        TextView textFlag;
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
        @SuppressWarnings("all")
        protected void bindData(CardUser bean, int position, OnItemClickListener onItemClickListener) {
            //优先加载相册作为封面，如果没有则加载头像
            if (!StrUtil.isEmpty(bean.getPhotoList())) {
                ImageLoaderHelper.loadImg(imgPic, bean.getPhotoList().get(0).getPhotoUrl());
            } else {
                ImageLoaderHelper.loadImg(imgPic, bean.getAvatar());
            }
            textName.setText(bean.getNickname());
            textLableGender.setSelected(!bean.isMale());
            textLableGender.setText(TimeUtils.getBirthdaySpan(bean.getBirthday()));
            textLableAstro.setText(TimeUtils.getAstro(bean.getBirthday()));
            textTag.setText(bean.getTagText());
            textPosition.setText(bean.getCity());
            textFlag.setText(bean.getPicCount() + "张图");

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
        void onItemClick(CardUser cardUser, Holder holder);

        void onItemScroll(CardUser cardUser, Holder holder);
    }
}
