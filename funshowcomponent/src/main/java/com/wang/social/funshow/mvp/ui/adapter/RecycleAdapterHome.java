package com.wang.social.funshow.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.mvp.IView;
import com.frame.utils.StrUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.funshow.Funshow;
import com.wang.social.funshow.mvp.entities.funshow.FunshowListRsc;
import com.wang.social.funshow.mvp.ui.dialog.MorePopupWindow;
import com.wang.social.funshow.net.helper.NetZanHelper;
import com.wang.social.funshow.utils.FunShowUtil;

import butterknife.BindView;

public class RecycleAdapterHome extends BaseAdapter<Funshow> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.funshow_item_home);
    }

    public class Holder extends BaseViewHolder<Funshow> {
        @BindView(R2.id.img_header)
        ImageView img_header;
        @BindView(R2.id.img_pic)
        ImageView imgPic;
        @BindView(R2.id.img_more)
        ImageView imgMore;
        @BindView(R2.id.text_name)
        TextView textName;
        @BindView(R2.id.text_time)
        TextView textTime;
        @BindView(R2.id.text_title)
        TextView textTitle;
        @BindView(R2.id.img_player)
        ImageView imgPlayer;
        @BindView(R2.id.img_tag_pay)
        ImageView imgTagPay;
        @BindView(R2.id.text_pic_count)
        TextView textPicCount;
        @BindView(R2.id.text_position)
        TextView textPosition;
        @BindView(R2.id.text_zan)
        TextView textZan;
        @BindView(R2.id.text_comment)
        TextView textComment;
        @BindView(R2.id.text_share)
        TextView textShare;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Funshow bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(img_header, bean.getUserCover());
            textName.setText(bean.getUserName());
            textTitle.setText(bean.getContent());
            textPosition.setText(bean.getProvince() + bean.getCity());
            textZan.setText(bean.getTalkSupportNum() + "");
            textComment.setText(bean.getTalkCommentNum() + "");
            textShare.setText(bean.getTalkShareNum() + "");
            textPicCount.setText("1/" + bean.getTalkImageNum());
            textTime.setText(FunShowUtil.getFunshowTimeStr(bean.getCreateTime()));
            imgTagPay.setVisibility(bean.isShopping() ? View.VISIBLE : View.GONE);
            textZan.setSelected(bean.isSupport());

            FunshowListRsc resource = bean.getFirstVideoOrImg();
            if (resource != null) {
                //TODO:暂未考虑视频的情况，如果是视频需要去获取第一帧图片
                ImageLoaderHelper.loadImg(imgPic, resource.getUrl());
            } else {
                imgPic.setImageResource(R.drawable.default_rect);
            }
            imgPlayer.setVisibility(bean.hasVideo() ? View.VISIBLE : View.GONE);

            imgMore.setOnClickListener(view -> {
                new MorePopupWindow(getContext()).showPopupWindow(view);
            });

            textZan.setOnClickListener(v -> {
                IView iView = (getContext() instanceof IView) ? (IView) getContext() : null;
                NetZanHelper.newInstance().funshowZan(iView, textZan, bean.getTalkId(), !textZan.isSelected(), (isZan, zanCount) -> {
                    bean.setIsSupport(isZan);
                    bean.setTalkSupportNum(zanCount);
                });
            });
            textShare.setOnClickListener(v -> {
//                SocializeUtil.shareWeb(getChildFragmentManager(),
//                        shareListener,
//                        "http://www.wangsocial.com/",
//                        "往往",
//                        "有点2的社交软件",
//                        "http://resouce.dongdongwedding.com/activity_cashcow_moneyTree.png");
            });
        }

        @Override
        public void onRelease() {
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }

    //////////////////////////////


    public Funshow getItemById(int talkId) {
        if (getData() == null) return null;
        for (Funshow funshow : getData()) {
            if (funshow.getTalkId() == talkId) {
                return funshow;
            }
        }
        return null;
    }

    public void refreshZanById(int talkId, boolean isZan, int zanCount) {
        if (StrUtil.isEmpty(getData())) return;
        for (int i = 0; i < getData().size(); i++) {
            Funshow funshow = getData().get(i);
            if (funshow.getTalkId() == talkId) {
                funshow.setTalkSupportNum(zanCount);
                funshow.setIsSupport(isZan);
                notifyItemChanged(i);
            }
        }
    }

    public void refreshCommentById(int talkId) {
        if (StrUtil.isEmpty(getData())) return;
        for (int i = 0; i < getData().size(); i++) {
            Funshow funshow = getData().get(i);
            if (funshow.getTalkId() == talkId) {
                funshow.setTalkCommentNum(funshow.getTalkCommentNum() + 1);
                notifyItemChanged(i);
            }
        }
    }
}
