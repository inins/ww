package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.utils.UIUtil;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.http.imageloader.glide.RoundedCornersTransformation;
import com.frame.utils.FrameUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.frame.component.enums.ConversationType;
import com.wang.social.im.helper.StickHelper;
import com.wang.social.im.mvp.model.entities.UIConversation;
import com.wang.social.im.mvp.ui.adapters.ConversationAdapter;
import com.wang.social.im.view.SwipeMenuLayout;
import com.wang.social.im.view.emotion.EmojiDisplay;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-17 10:48
 * ============================================
 */
public class ConversationViewHolder extends BaseViewHolder<UIConversation> {

    @BindView(R2.id.icv_iv_portrait)
    ImageView icvIvPortrait;
    @BindView(R2.id.icv_tv_name)
    TextView icvTvName;
    @BindView(R2.id.icv_tv_message)
    TextView icvTvMessage;
    @BindView(R2.id.icv_tv_time)
    TextView icvTvTime;
    @BindView(R2.id.icv_tv_unread)
    TextView icvTvUnread;
    @BindView(R2.id.im_tvb_stick)
    TextView imTvbStick;
    @BindView(R2.id.im_tvb_delete)
    TextView imTvbDelete;
    @BindView(R2.id.icv_cl_content)
    ConstraintLayout icvClContent;

    ImageLoader mImageLoader;

    private ConversationAdapter.OnHandleListener mHandleListener;

    public ConversationViewHolder(Context context, ViewGroup root, ConversationAdapter.OnHandleListener onHandleListener) {
        super(context, root, R.layout.im_item_conversation);
        mHandleListener = onHandleListener;
        mImageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
    }

    @Override
    protected void bindData(UIConversation itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        String name = itemValue.getName();
        String portrait = itemValue.getPortrait();

        icvTvName.setText(name);

        int defaultPortrait = R.drawable.im_round_image_placeholder;
        if (itemValue.getConversationType() == ConversationType.SOCIAL) {
            defaultPortrait = R.drawable.im_placeholder_social;
        } else if (itemValue.getConversationType() == ConversationType.TEAM) {
            defaultPortrait = R.drawable.im_placeholder_team;
        }

        mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                .errorPic(defaultPortrait)
                .placeholder(defaultPortrait)
                .transformation(new RoundedCornersTransformation(UIUtil.getDimen(R.dimen.im_round_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                .imageView(icvIvPortrait)
                .url(portrait)
                .build());

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

        if (StickHelper.getInstance().isStick(itemValue)) {
            imTvbStick.setText(R.string.im_cvs_unstick);
        } else {
            imTvbStick.setText(R.string.im_cvs_stick);
        }

        ((SwipeMenuLayout) itemView).quickClose();

        imTvbStick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SwipeMenuLayout) itemView).smoothClose();
                if (mHandleListener != null) {
                    mHandleListener.toggleStick(itemValue, position);
                }
            }
        });
        imTvbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SwipeMenuLayout) itemView).smoothClose();
                if (mHandleListener != null) {
                    mHandleListener.onDelete(itemValue, position);
                }
            }
        });

        icvClContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(itemValue, position);
                }
            }
        });
    }

    @Override
    public void onRelease() {
        super.onRelease();
        mImageLoader.clear(getContext(), ImageConfigImpl.builder()
                .imageView(icvIvPortrait)
                .build());
        mImageLoader = null;
    }
}
