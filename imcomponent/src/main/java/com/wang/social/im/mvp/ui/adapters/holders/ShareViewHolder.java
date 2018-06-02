package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.utils.UIUtil;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.google.gson.Gson;
import com.tencent.imsdk.TIMCustomElem;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.enums.CustomElemType;
import com.wang.social.im.mvp.model.entities.ShareElemData;
import com.wang.social.im.mvp.model.entities.UIMessage;

import java.io.File;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-25 15:06
 * ============================================
 */
public class ShareViewHolder extends BaseMessageViewHolder<UIMessage> {

    @BindView(R2.id.msg_iv_share)
    ImageView msgIvShare;
    @BindView(R2.id.msg_tv_share_title)
    TextView msgTvShareTitle;
    @BindView(R2.id.msg_tv_share_content)
    TextView msgTvShareContent;
    @BindView(R2.id.msg_v_share_line)
    View msgVShareLine;
    @BindView(R2.id.msg_tv_share_source)
    TextView msgTvShareSource;
    @BindView(R2.id.msg_cl_share)
    ConstraintLayout msgClShare;
    @BindView(R2.id.msg_iv_error)
    @Nullable
    ImageView msgIvError;
    @BindView(R2.id.msg_pb_progress)
    @Nullable
    ContentLoadingProgressBar msgPbProgress;

    private Gson mGson;

    public ShareViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
        mGson = FrameUtils.obtainAppComponentFromContext(context).gson();
        mImageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
    }

    @Override
    protected void initStyle(UIMessage uiMessage) {
        //do nothing
    }

    @Override
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        super.bindData(itemValue, position, onItemClickListener);

        //获取自定义消息类型
        TIMCustomElem customElem = (TIMCustomElem) itemValue.getTimMessage().getElement(0);
        CustomElemType elemType = CustomElemType.getElemType(customElem);
        ShareElemData elemData = (ShareElemData) itemValue.getCustomMessageElemData(elemType, ShareElemData.class, mGson);
        if (elemData != null) {
            msgTvShareTitle.setText(elemData.getTitle());
            msgTvShareContent.setText(elemData.getContent());
            mImageLoader.loadImage(getContext(), ImageConfigImpl
                    .builder()
                    .placeholder(R.drawable.im_image_placeholder)
                    .errorPic(R.drawable.im_image_placeholder)
                    .imageView(msgIvShare)
                    .url(elemData.getCover())
                    .build());
            if (elemType == CustomElemType.SHARE_FUN_SHOW) {
                msgTvShareSource.setText(UIUtil.getString(R.string.im_from_format, "趣晒"));
            } else if (elemType == CustomElemType.SHARE_TOPIC) {
                msgTvShareSource.setText(UIUtil.getString(R.string.im_from_format, "话题"));
            }
        }

        if (itemValue.getTimMessage().isSelf()) {
            showStatus(itemValue, msgIvError, msgPbProgress);
            setErrorListener(msgIvError, itemValue, position);
        } else {
            setPortraitListener(msgIvPortrait, itemValue, position);
        }
        setContentListener(msgClShare, itemValue, position, true, true);
    }
}
