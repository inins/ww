package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.google.gson.Gson;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.enums.CustomElemType;
import com.wang.social.im.mvp.model.entities.EnvelopElemData;
import com.wang.social.im.mvp.model.entities.EnvelopMessageCacheInfo;
import com.wang.social.im.mvp.model.entities.UIMessage;

import butterknife.BindView;

/**
 * ============================================
 * 红包消息
 * <p>
 * Create by ChenJing on 2018-04-25 9:41
 * ============================================
 */
public class EnvelopViewHolder extends BaseMessageViewHolder<UIMessage> {

    @BindView(R2.id.msg_tv_time)
    TextView msgTvTime;
    @BindView(R2.id.msg_iv_portrait)
    ImageView msgIvPortrait;
    @BindView(R2.id.msg_tv_name)
    @Nullable
    TextView msgTvName;
    @BindView(R2.id.msg_tv_message)
    TextView msgTvMessage;
    @BindView(R2.id.msg_tv_status)
    TextView msgTvStatus;
    @BindView(R2.id.msg_cl_envelop)
    ConstraintLayout msgClEnvelop;
    @BindView(R2.id.msg_iv_error)
    @Nullable
    ImageView msgIvError;
    @BindView(R2.id.msg_pb_progress)
    @Nullable
    ContentLoadingProgressBar msgPbProgress;

    private Gson gson;

    public EnvelopViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
        gson = FrameUtils.obtainAppComponentFromContext(context).gson();
    }

    @Override
    protected void initStyle(UIMessage uiMessage) {

    }

    @Override
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        if (itemValue.isShowTime()) {
            msgTvTime.setVisibility(View.VISIBLE);
            msgTvTime.setText(getTimeStr(itemValue.getTimMessage().timestamp()));
        } else {
            msgTvTime.setVisibility(View.GONE);
        }

        if (showNickname && msgTvName != null) {
            msgTvName.setVisibility(View.VISIBLE);
            msgTvName.setText(itemValue.getNickname(conversationType));
        } else if (msgTvName != null) {
            msgTvName.setVisibility(View.GONE);
        }

        //头像
        if (showHeader) {
            msgIvPortrait.setVisibility(View.VISIBLE);
            mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                    .placeholder(R.drawable.common_default_circle_placeholder)
                    .imageView(msgIvPortrait)
                    .isCircle(true)
                    .url(itemValue.getPortrait(conversationType))
                    .build());
        } else {
            msgIvPortrait.setVisibility(View.GONE);
        }

        //显示红包信息
        EnvelopElemData elem = (EnvelopElemData) itemValue.getCustomMessageElemData(CustomElemType.RED_ENVELOP, gson);
        if (elem != null){
            msgTvMessage.setText(elem.getMessage() == null ? "" : elem.getMessage());
            EnvelopMessageCacheInfo cacheInfo = itemValue.getEnvelopCacheInfo(gson);
            if (cacheInfo == null || cacheInfo.getStatus() == EnvelopMessageCacheInfo.STATUS_INITIAL){
                msgTvStatus.setText(R.string.im_envelop_status_adopt);
                if (itemValue.getTimMessage().isSelf()){
                    msgClEnvelop.setBackgroundResource(R.drawable.im_bg_msg_envelop_right);
                }else {
                    msgClEnvelop.setBackgroundResource(R.drawable.im_bg_msg_envelop_left);
                }
            }else {
                if (cacheInfo.getStatus() == EnvelopMessageCacheInfo.STATUS_ADOPTED){
                    msgTvStatus.setText(R.string.im_envelop_status_adopted);
                }else if (cacheInfo.getStatus() == EnvelopMessageCacheInfo.STATUS_OVERDUE){
                    msgTvStatus.setText(R.string.im_envelop_status_overdue);
                }
                if (itemValue.getTimMessage().isSelf()){
                    msgClEnvelop.setBackgroundResource(R.drawable.im_bg_msg_envelop_right_adopted);
                }else {
                    msgClEnvelop.setBackgroundResource(R.drawable.im_bg_msg_envelop_left_adopted);
                }
            }
        }

        if (itemValue.getTimMessage().isSelf()) {
            showStatus(itemValue, msgIvError, msgPbProgress);
            setErrorListener(msgIvError, itemValue, position);
        } else {
            setPortraitListener(msgIvPortrait, itemValue, position);
        }
        setContentListener(msgClEnvelop, itemValue, position, true, true);
    }

    @Override
    public void onRelease() {
        super.onRelease();
        gson = null;
    }
}