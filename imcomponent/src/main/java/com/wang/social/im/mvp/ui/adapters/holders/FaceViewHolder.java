package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.enums.ConversationType;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.tencent.imsdk.TIMFaceElem;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.view.emotion.EmojiDisplay;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-18 17:00
 * ============================================
 */
public class FaceViewHolder extends BaseMessageViewHolder<UIMessage> {

    @BindView(R2.id.msg_iv_portrait)
    ImageView msgIvPortrait;
    @BindView(R2.id.msg_iv_face)
    ImageView msgIvFace;
    @BindView(R2.id.msg_tv_time)
    TextView msgTvTime;
    @BindView(R2.id.msg_tv_name)
    @Nullable
    TextView msgTvName;
    @BindView(R2.id.msg_iv_error)
    @Nullable
    ImageView msgIvError;
    @BindView(R2.id.msg_pb_progress)
    @Nullable
    ContentLoadingProgressBar msgPbProgress;

    public FaceViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
    }

    @Override
    protected void initStyle(UIMessage uiMessage) {
        if (conversationType == ConversationType.MIRROR) {
            if (msgTvName != null) {
                msgTvName.setTextColor(ContextCompat.getColor(getContext(), R.color.im_bg_message_mirror_left));
            }
            msgTvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.im_message_mirror_left_text));
        } else {
            if (msgTvName != null) {
                msgTvName.setTextColor(ContextCompat.getColor(getContext(), R.color.im_message_txt_receive));
            }
            msgTvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.common_text_dark_light));
        }
    }

    @Override
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        initStyle(itemValue);

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

        TIMFaceElem faceElem = (TIMFaceElem) itemValue.getMessageElem(TIMFaceElem.class);
        if (faceElem != null) {
            int index = faceElem.getIndex();
            String faceDrawableName = "im_ww_emoji_";
            if (index < 10) {
                faceDrawableName = faceDrawableName + "00" + index;
            } else if (index < 100) {
                faceDrawableName = faceDrawableName + "0" + index;
            } else {
                faceDrawableName = faceDrawableName + index;
            }
            msgIvFace.setImageDrawable(EmojiDisplay.getDrawable(getContext(), faceDrawableName));
        }

        if (itemValue.getTimMessage().isSelf()) {
            showStatus(itemValue, msgIvError, msgPbProgress);
            setErrorListener(msgIvError, itemValue, position);
        } else {
            setPortraitListener(msgIvPortrait, itemValue, position);
        }
        setContentListener(msgIvFace, itemValue, position, false, true);
    }
}
