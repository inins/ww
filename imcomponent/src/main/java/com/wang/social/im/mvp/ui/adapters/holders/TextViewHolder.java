package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.enums.ConversationType;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.view.emotion.EmojiDisplay;

import butterknife.BindView;

/**
 * ==========================================
 * 文本消息
 * <p>
 * Create by ChenJing on 2018-04-02 16:49
 * ==========================================
 */
public class TextViewHolder extends BaseMessageViewHolder<UIMessage> {

    @BindView(R2.id.msg_iv_portrait)
    ImageView msgIvPortrait;
    @BindView(R2.id.msg_tv_text)
    TextView msgTvText;
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

    public TextViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
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

        TIMMessage timMessage = itemValue.getTimMessage();
        for (int i = 0, max = (int) timMessage.getElementCount(); i < max; i++) {
            TIMElem elem = timMessage.getElement(i);
            if (elem instanceof TIMTextElem) {
                TIMTextElem textElem = (TIMTextElem) elem;
                msgTvText.setText(EmojiDisplay.spannableFilter(getContext(), new SpannableString(textElem.getText()), textElem.getText(), getContext().getResources().getDimensionPixelSize(R.dimen.im_msg_text_size)));
                break;
            }
        }

        if (timMessage.isSelf()) {
            showStatus(itemValue, msgIvError, msgPbProgress);
            setErrorListener(msgIvError, itemValue, position);
        } else {
            setPortraitListener(msgIvPortrait, itemValue, position);
        }
        setContentListener(msgTvText, itemValue, position, false, true);
    }

    /**
     * 若会话类型为镜子聊天则修改UI样式
     *
     * @param uiMessage
     */
    protected void initStyle(UIMessage uiMessage) {
        if (conversationType == ConversationType.MIRROR) {
            if (uiMessage.getTimMessage().isSelf()) {
                msgTvText.setBackgroundResource(R.drawable.im_bg_message_right_mirror);
                msgTvText.setTextColor(Color.WHITE);
            } else {
                msgTvText.setBackgroundResource(R.drawable.im_bg_message_left_mirror);
                msgTvText.setTextColor(ContextCompat.getColor(getContext(), R.color.im_message_mirror_left_text));
            }
            if (msgTvName != null) {
                msgTvName.setTextColor(ContextCompat.getColor(getContext(), R.color.im_bg_message_mirror_left));
            }
            msgTvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.im_message_mirror_left_text));
        } else {
            if (uiMessage.getTimMessage().isSelf()) {
                msgTvText.setBackgroundResource(R.drawable.im_bg_message_right);
                msgTvText.setTextColor(Color.WHITE);
            } else {
                if (conversationType == ConversationType.TEAM) {
                    msgTvText.setBackgroundResource(R.drawable.im_bg_message_left_team);
                } else {
                    msgTvText.setBackgroundResource(R.drawable.im_bg_message_left);
                }
                msgTvText.setTextColor(ContextCompat.getColor(getContext(), R.color.im_message_txt_receive));
            }
            if (msgTvName != null) {
                msgTvName.setTextColor(ContextCompat.getColor(getContext(), R.color.im_message_txt_receive));
            }
            msgTvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.common_text_dark_light));
        }
    }

    @Override
    public void onRelease() {
        mUnbinder.unbind();
        mUnbinder = null;
        if (showHeader) {
            mImageLoader.clear(getContext(), ImageConfigImpl.builder()
                    .imageView(msgIvPortrait)
                    .build());
        }
    }
}
