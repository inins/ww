package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.text.SpannableString;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.utils.UIUtil;
import com.tencent.imsdk.TIMTextElem;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.enums.ConversationType;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.view.emotion.EmojiDisplay;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-11 11:49
 * ============================================
 */
public class GameTextViewHolder extends BaseMessageViewHolder<UIMessage> {

    @BindView(R2.id.igt_tv_name)
    TextView igtTvName;
    @BindView(R2.id.igt_tv_message)
    TextView igtTvMessage;

    public GameTextViewHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.im_item_msg_game_text);
    }

    @Override
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        TIMTextElem textElem = (TIMTextElem) itemValue.getMessageElem(TIMTextElem.class);
        if (textElem != null) {
            igtTvMessage.setText(EmojiDisplay.spannableFilter(getContext(), new SpannableString(textElem.getText()), textElem.getText(), getContext().getResources().getDimensionPixelSize(R.dimen.im_msg_text_size)));
        }
        if (itemValue.getTimMessage().isSelf()) {
            igtTvName.setText(UIUtil.getString(R.string.im_myself));
            igtTvName.setTextColor(0XFFD39F1B);
        } else {
            igtTvName.setText(String.format("%s :", itemValue.getNickname(ConversationType.PRIVATE)));
            igtTvName.setTextColor(0xFFCE4D12);
        }
    }

    @Override
    protected void initStyle(UIMessage uiMessage) {

    }
}