package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.enums.ConversationType;
import com.frame.component.utils.UIUtil;
import com.tencent.imsdk.TIMFaceElem;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.view.emotion.EmojiDisplay;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-11 13:43
 * ============================================
 */
public class GameEmotionViewHolder extends BaseMessageViewHolder<UIMessage> {

    @BindView(R2.id.igt_tv_name)
    TextView igtTvName;
    @BindView(R2.id.igt_iv_face)
    ImageView igtIvFace;

    public GameEmotionViewHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.im_item_msg_game_face);
    }

    @Override
    protected void initStyle(UIMessage uiMessage) {

    }

    @Override
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        if (itemValue.getTimMessage().isSelf()) {
            igtTvName.setText(UIUtil.getString(R.string.im_myself));
            igtTvName.setTextColor(0XFFD39F1B);
        } else {
            igtTvName.setText(String.format("%s :", itemValue.getNickname(ConversationType.PRIVATE)));
            igtTvName.setTextColor(0xFFCE4D12);
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
            igtIvFace.setImageDrawable(EmojiDisplay.getDrawable(getContext(), faceDrawableName));
        }
    }
}
