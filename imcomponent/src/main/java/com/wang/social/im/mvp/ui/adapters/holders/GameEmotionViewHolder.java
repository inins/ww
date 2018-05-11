package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.view.ViewGroup;

import com.frame.base.BaseAdapter;
import com.wang.social.im.mvp.model.entities.UIMessage;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-11 13:43
 * ============================================
 */
public class GameEmotionViewHolder extends BaseMessageViewHolder<UIMessage>{

    public GameEmotionViewHolder(Context context, ViewGroup root) {
        super(context, root, 0);
    }

    @Override
    protected void initStyle(UIMessage uiMessage) {

    }

    @Override
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {

    }
}
