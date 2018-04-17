package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.view.ViewGroup;

import com.frame.base.BaseAdapter;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.UIMessage;

/**
 * ============================================
 * 未知消息，展示空白行
 * <p>
 * Create by ChenJing on 2018-04-17 17:06
 * ============================================
 */
public class UnknownViewHolder extends BaseMessageViewHolder<UIMessage> {

    public UnknownViewHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.im_item_msg_unknown);
    }

    @Override
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {

    }
}
