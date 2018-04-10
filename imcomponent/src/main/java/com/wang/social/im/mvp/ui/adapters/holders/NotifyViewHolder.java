package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.utils.FrameUtils;
import com.google.gson.Gson;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.enums.CustomElemType;
import com.wang.social.im.mvp.model.entities.RevokeElem;
import com.wang.social.im.mvp.model.entities.UIMessage;

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

        //根据通知Elem来判断显示内容
        int elemCount = (int) itemValue.getTimMessage().getElementCount();
        for (int i = 0; i < elemCount; i++) {
            TIMElem timElem = timMessage.getElement(i);
            if (timElem instanceof TIMCustomElem) {
                TIMCustomElem customElem = (TIMCustomElem) timElem;
                CustomElemType elemType = CustomElemType.getElemType(customElem);
                if (elemType == CustomElemType.REVOKE) {
                    showRevokeTip(customElem);
                    break;
                }
            }
        }
    }

    /**
     * 显示撤回通知
     */
    private void showRevokeTip(TIMCustomElem customElem) {
        String jsonData = new String(customElem.getData());
        RevokeElem revokeElem = gson.fromJson(jsonData, RevokeElem.class);
        // TODO: 2018-04-10 修改sender为昵称
        String sender = revokeElem.getSender();
        if (revokeElem.getSender().equals(TIMManager.getInstance().getLoginUser())) {
            sender = getContext().getResources().getString(R.string.im_self);
        }
        String showMessage = sender + getContext().getResources().getString(R.string.im_message_tip_revoke);
        mnTvNotify.setText(showMessage);
    }

    @Override
    public void onRelease() {
        super.onRelease();
        gson = null;
    }
}
