package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.UIConversation;
import com.wang.social.im.view.emotion.EmojiDisplay;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-17 10:48
 * ============================================
 */
public class ConversationViewHolder extends BaseViewHolder<UIConversation> {

    @BindView(R.id.icv_iv_portrait)
    ImageView icvIvPortrait;
    @BindView(R.id.icv_tv_name)
    TextView icvTvName;
    @BindView(R.id.icv_tv_message)
    TextView icvTvMessage;
    @BindView(R.id.icv_tv_time)
    TextView icvTvTime;
    @BindView(R.id.icv_tv_unread)
    TextView icvTvUnread;

    ImageLoader mImageLoader;

    public ConversationViewHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.im_item_conversation);
        mImageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
    }

    @Override
    protected void bindData(UIConversation itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        String name = itemValue.getName();
        String portrait = itemValue.getPortrait();

        icvTvName.setText(name);
        if (!TextUtils.isEmpty(portrait)) {
            mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                    .errorPic(R.drawable.default_header)
                    .placeholder(R.drawable.default_header)
                    .imageView(icvIvPortrait)
                    .url(portrait)
                    .build());
        } else {
            icvIvPortrait.setImageResource(R.drawable.common_default_circle_placeholder);
        }

        String summary = itemValue.getLastMessageSummary();
        icvTvMessage.setText(EmojiDisplay.spannableFilter(getContext(), new SpannableString(summary), summary, getContext().getResources().getDimensionPixelSize(R.dimen.im_cvs_message_txt_size)));
        icvTvTime.setText(itemValue.getTime());
        int unreadNum = itemValue.getUnreadNum();
        if (unreadNum > 0) {
            icvTvUnread.setVisibility(View.VISIBLE);
            if (unreadNum > 99) {
                icvTvUnread.setText(R.string.im_cvs_unread_max);
            } else {
                icvTvUnread.setText(String.valueOf(unreadNum));
            }
        } else {
            icvTvUnread.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean useItemClickListener() {
        return true;
    }

    @Override
    public void onRelease() {
        super.onRelease();
        mImageLoader = null;
    }
}
