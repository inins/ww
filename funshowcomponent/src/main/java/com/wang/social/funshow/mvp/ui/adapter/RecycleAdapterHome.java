package com.wang.social.funshow.mvp.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.wang.social.funshow.utils.VideoCoverUtil;
import com.wang.social.socialize.SocializeUtil;

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
        MorePopupWindow popupWindow;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
            popupWindow = new MorePopupWindow(getContext());
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
            imgTagPay.setVisibility(bean.isFree() ? View.VISIBLE : View.GONE);
            textZan.setSelected(bean.isSupport());


            imgPlayer.setVisibility(bean.hasVideo() ? View.VISIBLE : View.GONE);
            if (bean.hasVideo()) {
                Bitmap coverbitmap = VideoCoverUtil.createVideoThumbnail(bean.getResourceUrl().getUrl());
                imgPic.setImageBitmap(coverbitmap);
            } else {
                FunshowListRsc imgRsc = bean.getFirstImg();
                if (imgRsc != null) {
                    ImageLoaderHelper.loadImg(imgPic, imgRsc.getUrl());
                } else {
                    imgPic.setImageResource(R.drawable.default_rect);
                }
            }

            imgMore.setOnClickListener(view -> {
                popupWindow.setOnDislikeClickListener(v -> {
                    if (onDislikeClickListener != null)
                        onDislikeClickListener.onDislikeClick(v, bean);
                });
                popupWindow.showPopupWindow(view);
            });

            textZan.setOnClickListener(v -> {
                IView iView = (getContext() instanceof IView) ? (IView) getContext() : null;
                NetZanHelper.newInstance().funshowZan(iView, textZan, bean.getTalkId(), !textZan.isSelected(), (isZan, zanCount) -> {
                    bean.setIsSupport(isZan);
                    bean.setTalkSupportNum(zanCount);
                });
            });
            textShare.setOnClickListener(v -> {
                if (onShareClickListener != null) onShareClickListener.onShareClick(v, bean);
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


    public int getIndexById(int talkId) {
        if (getData() == null) return -1;
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).getTalkId() == talkId) {
                return i;
            }
        }
        return -1;
    }

    public void refreshZanById(int talkId, boolean isZan, int zanCount) {
        int index = getIndexById(talkId);
        if (index != -1) {
            Funshow funshow = getData().get(index);
            funshow.setTalkSupportNum(zanCount);
            funshow.setIsSupport(isZan);
            notifyItemChanged(index);
        }
//        if (StrUtil.isEmpty(getData())) return;
//        for (int i = 0; i < getData().size(); i++) {
//            Funshow funshow = getData().get(i);
//            if (funshow.getTalkId() == talkId) {
//                funshow.setTalkSupportNum(zanCount);
//                funshow.setIsSupport(isZan);
//                notifyItemChanged(i);
//            }
//        }
    }

    public void refreshCommentById(int talkId) {
        int index = getIndexById(talkId);
        if (index != -1) {
            Funshow funshow = getData().get(index);
            funshow.setTalkCommentNum(funshow.getTalkCommentNum() + 1);
            notifyItemChanged(index);
        }
//        if (StrUtil.isEmpty(getData())) return;
//        for (int i = 0; i < getData().size(); i++) {
//            Funshow funshow = getData().get(i);
//            if (funshow.getTalkId() == talkId) {
//                funshow.setTalkCommentNum(funshow.getTalkCommentNum() + 1);
//                notifyItemChanged(i);
//            }
//        }
    }

    public void refreshShareById(int talkId) {
        int index = getIndexById(talkId);
        if (index != -1) {
            Funshow funshow = getData().get(index);
            funshow.setTalkShareNum(funshow.getTalkShareNum() + 1);
            notifyItemChanged(index);
        }
    }

    public void refreshNeedPayById(int talkId) {
        int index = getIndexById(talkId);
        if (index != -1) {
            Funshow funshow = getData().get(index);
            funshow.setIsShopping(false);
            notifyItemChanged(index);
        }
//        if (StrUtil.isEmpty(getData())) return;
//        for (int i = 0; i < getData().size(); i++) {
//            Funshow funshow = getData().get(i);
//            if (funshow.getTalkId() == talkId) {
//                funshow.setIsShopping(false);
//                notifyItemChanged(i);
//            }
//        }
    }

    /////////////////////////////

    public interface OnDislikeClickListener {
        void onDislikeClick(View v, Funshow funshow);
    }

    private OnDislikeClickListener onDislikeClickListener;

    public void setOnDislikeClickListener(OnDislikeClickListener onDislikeClickListener) {
        this.onDislikeClickListener = onDislikeClickListener;
    }

    public interface OnShareClickListener {
        void onShareClick(View v, Funshow funshow);
    }

    private OnShareClickListener onShareClickListener;

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }
}
