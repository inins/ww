package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.view.ViewGroup;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.wang.social.im.mvp.model.entities.UIConversation;
import com.wang.social.im.mvp.ui.adapters.holders.ConversationViewHolder;

import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-17 10:05
 * ============================================
 */
public class ConversationAdapter extends BaseAdapter<UIConversation> {

    private static final int VIEW_HOLDER_FREE = 0;
    private static final int VIEW_HOLDER_OFFICIAL = 1;

    public ConversationAdapter(List<UIConversation> conversations) {
        this.valueList = conversations;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        if (viewType == VIEW_HOLDER_OFFICIAL) {
            return null;
        } else {
            return new ConversationViewHolder(context, parent);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_HOLDER_FREE;
    }
}