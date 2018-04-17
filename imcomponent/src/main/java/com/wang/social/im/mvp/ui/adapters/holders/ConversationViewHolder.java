package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.http.imageloader.ImageLoader;
import com.frame.utils.FrameUtils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.wang.social.im.R;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-17 10:48
 * ============================================
 */
public class ConversationViewHolder extends BaseViewHolder<TIMConversation> {

    @BindView(R.id.icv_iv_portrait)
    ImageView icvIvPortrait;
    @BindView(R.id.icv_tv_name)
    TextView icvTvName;
    @BindView(R.id.icv_tv_message)
    TextView icvTvMessage;
    @BindView(R.id.icv_tv_time)
    TextView icvTvTime;

    ImageLoader mImageLoader;

    public ConversationViewHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.im_item_conversation);
        mImageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
    }

    @Override
    protected void bindData(TIMConversation itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        if (itemValue.getType() == TIMConversationType.C2C){

        }else if (itemValue.getType() == TIMConversationType.Group){

        }
    }

    @Override
    public void onRelease() {
        super.onRelease();
        mImageLoader = null;
    }
}
