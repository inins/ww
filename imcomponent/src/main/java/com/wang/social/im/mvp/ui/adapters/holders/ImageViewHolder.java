package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.view.ViewGroup;

import com.frame.base.BaseAdapter;
import com.wang.social.im.R;
import com.wang.social.im.entities.UIMessage;

/**
 * ======================================
 * 图片
 * <p>
 * Create by ChenJing on 2018-04-03 10:43
 * ======================================
 */
public class ImageViewHolder extends BaseMessageViewHolder<UIMessage>{

    public ImageViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, R.layout.im_item_msg_image_left);
    }

    @Override
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {

    }

    @Override
    public void onRelease() {

    }
}