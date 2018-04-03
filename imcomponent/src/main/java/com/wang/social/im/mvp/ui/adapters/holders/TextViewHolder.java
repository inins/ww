package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.frame.base.BaseAdapter;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.TimeUtils;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMUserProfile;
import com.wang.social.im.R;
import com.wang.social.im.entities.UIMessage;
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

    @BindView(R.id.msg_iv_portrait)
    ImageView msgIvPortrait;
    @BindView(R.id.msg_tv_text)
    TextView msgTvText;
    @BindView(R.id.msg_tv_time)
    TextView msgTvTime;
    @BindView(R.id.msg_tv_name)
    TextView msgTvName;

    public TextViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
    }

    @Override
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        if (showTimestamp){
            msgTvTime.setVisibility(View.VISIBLE);
            msgTvTime.setText(getTimeStr(itemValue.getTimMessage().timestamp()));
        }else {
            msgTvTime.setVisibility(View.GONE);
        }

        TIMUserProfile profile = itemValue.getTimMessage().getSenderProfile();
        mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                .placeholder(R.drawable.common_default_placehohlder)
                .imageView(msgIvPortrait)
                .isCircle(true)
                .url(profile.getFaceUrl())
                .build());
        if (!itemValue.getTimMessage().isSelf()){
            msgTvName.setVisibility(View.VISIBLE);
            msgTvName.setText(profile.getRemark());
        }else {
            msgTvName.setVisibility(View.GONE);
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
    }

    @Override
    public void onRelease() {
        mUnbinder.unbind();
        mUnbinder = null;
        mImageLoader.clear(getContext(), ImageConfigImpl.builder()
                .imageView(msgIvPortrait)
                .isClearMemory(true)
                .build());
    }
}