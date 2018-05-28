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
import com.tencent.imsdk.ext.message.TIMMessageExt;
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
        super.bindData(itemValue, position, onItemClickListener);

        //显示红包信息
        EnvelopElemData elem = (EnvelopElemData) itemValue.getCustomMessageElemData(CustomElemType.RED_ENVELOP, EnvelopElemData.class, gson);
        if (elem != null) {
            msgTvMessage.setText(elem.getMessage() == null ? "" : elem.getMessage());
            TIMMessageExt messageExt = new TIMMessageExt(itemValue.getTimMessage());
            int status = messageExt.getCustomInt();
            if (status == EnvelopMessageCacheInfo.STATUS_INITIAL) {
                msgTvStatus.setText(R.string.im_envelop_status_adopt);
                if (itemValue.getTimMessage().isSelf()) {
                    msgClEnvelop.setBackgroundResource(R.drawable.im_bg_msg_envelop_right);
                } else {
                    msgClEnvelop.setBackgroundResource(R.drawable.im_bg_msg_envelop_left);
                }
            } else {
                if (status == EnvelopMessageCacheInfo.STATUS_ADOPTED) {
                    msgTvStatus.setText(R.string.im_envelop_status_adopted);
                } else if (status == EnvelopMessageCacheInfo.STATUS_OVERDUE) {
                    msgTvStatus.setText(R.string.im_envelop_status_overdue);
                } else if (status == EnvelopMessageCacheInfo.STATUS_EMPTY) {
                    msgTvStatus.setText(R.string.im_envelop_status_empty);
                }
                if (itemValue.getTimMessage().isSelf()) {
                    msgClEnvelop.setBackgroundResource(R.drawable.im_bg_msg_envelop_right_adopted);
                } else {
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