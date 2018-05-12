package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.utils.UIUtil;
import com.frame.utils.FrameUtils;
import com.google.gson.Gson;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.enums.CustomElemType;
import com.wang.social.im.mvp.model.entities.GameNotifyElemData;
import com.wang.social.im.mvp.model.entities.UIMessage;

import java.lang.annotation.ElementType;

import butterknife.BindView;

/**
 * ============================================
 * 通知消息
 * <p>
 * Create by ChenJing on 2018-04-10 11:27
 * ============================================
 */
public class NotifyViewHolder extends BaseMessageViewHolder<UIMessage> {

    @BindView(R2.id.mn_tv_notify)
    TextView mnTvNotify;

    Gson gson;

    public NotifyViewHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.im_item_msg_notify);
        gson = FrameUtils.obtainAppComponentFromContext(context).gson();
    }

    @Override
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        TIMMessage timMessage = itemValue.getTimMessage();

        if (timMessage.status() == TIMMessageStatus.HasRevoked) {
            mnTvNotify.setText(itemValue.getRevokeSummary());
        } else {
            //根据通知Elem来判断显示内容
            int elemCount = (int) itemValue.getTimMessage().getElementCount();
            for (int i = 0; i < elemCount; i++) {
                TIMElem timElem = timMessage.getElement(i);
                if (timElem instanceof TIMCustomElem) {
                    TIMCustomElem customElem = (TIMCustomElem) timElem;
                    CustomElemType elemType = CustomElemType.getElemType(customElem);
                    if (elemType == CustomElemType.GAME_NOTIFY) { //游戏通知
                        GameNotifyElemData elemData = (GameNotifyElemData) itemValue.getCustomMessageElemData(CustomElemType.GAME_NOTIFY, gson);
                        showGameNotify(elemData);
                    }
                }
            }
        }

    }

    @Override
    public void onRelease() {
        super.onRelease();
        gson = null;
    }

    @Override
    protected void initStyle(UIMessage uiMessage) {

    }

    /**
     * 游戏通知
     *
     * @param data
     */
    private void showGameNotify(GameNotifyElemData data) {
        switch (data.getNotifyType()) {
            case CREATE:
                mnTvNotify.setText(UIUtil.getString(R.string.im_create_notify_format, data.getOperatorNickname()));
                break;
            case JOIN:
                mnTvNotify.setText(UIUtil.getString(R.string.im_join_notify_format, data.getOperatorNickname()));
                break;
            case RESULT:
                mnTvNotify.setVisibility(View.GONE);
                break;
        }
    }
}
