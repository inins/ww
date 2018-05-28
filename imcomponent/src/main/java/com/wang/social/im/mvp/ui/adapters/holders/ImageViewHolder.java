package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.http.imageloader.glide.RoundedCornersTransformation;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMImage;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMImageType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.frame.component.enums.ConversationType;
import com.wang.social.im.helper.ImHelper;
import com.wang.social.im.mvp.model.entities.UIMessage;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * ======================================
 * 图片
 * <p>
 * Create by ChenJing on 2018-04-03 10:43
 * ======================================
 */
public class ImageViewHolder extends BaseMessageViewHolder<UIMessage> {

    @BindView(R2.id.msg_iv_image)
    ImageView msgIvImage;
    @BindView(R2.id.msg_iv_error)
    @Nullable
    ImageView msgIvError;
    @BindView(R2.id.msg_pb_progress)
    @Nullable
    ContentLoadingProgressBar msgPbProgress;

    public ImageViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
    }

    @Override
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        initStyle(itemValue);

        super.bindData(itemValue, position, onItemClickListener);

        //图片
        TIMMessage timMessage = itemValue.getTimMessage();
        for (int i = 0, max = (int) timMessage.getElementCount(); i < max; i++) {
            TIMElem elem = timMessage.getElement(i);
            if (elem instanceof TIMImageElem) {
                TIMImageElem imageElem = (TIMImageElem) elem;

                if (timMessage.status() == TIMMessageStatus.Sending || timMessage.status() == TIMMessageStatus.SendFail) {
                    loadImage(imageElem.getPath());
                } else if (timMessage.status() == TIMMessageStatus.SendSucc) {
                    for (TIMImage image : imageElem.getImageList()) {
                        if (image.getType() == TIMImageType.Thumb) {
                            if (ImHelper.isCacheFileExit(image.getUuid())) {
                                loadImage(ImHelper.getImageCachePath(image.getUuid()));
                            } else {
                                image.getImage(ImHelper.getImageCachePath(image.getUuid()), new TIMCallBack() {
                                    @Override
                                    public void onError(int i, String s) {
                                        loadImage("");
                                    }

                                    @Override
                                    public void onSuccess() {
                                        loadImage(ImHelper.getImageCachePath(image.getUuid()));
                                    }
                                });
                            }
                        }
                    }
                    ArrayList<TIMImage> images = imageElem.getImageList();
                    if (images.size() > 0) {

                        loadImage(images.get(0).getUrl());
                        break;
                    }
                }
            }
        }

        if (timMessage.isSelf()) {
            showStatus(itemValue, msgIvError, msgPbProgress);
            setErrorListener(msgIvError, itemValue, position);
        } else {
            setPortraitListener(msgIvPortrait, itemValue, position);
        }
        setContentListener(msgIvImage, itemValue, position, true, true);
    }

    private void loadImage(String path) {
        mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                .imageView(msgIvImage)
                .url(path)
                .placeholder(R.drawable.im_image_message_placeholder)
                .transformation(new RoundedCornersTransformation(getContext().getResources().getDimensionPixelOffset(R.dimen.im_msg_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                .build());
    }

    @Override
    protected void initStyle(UIMessage uiMessage) {
        if (conversationType == ConversationType.MIRROR) {
            if (msgTvName != null) {
                msgTvName.setTextColor(ContextCompat.getColor(getContext(), R.color.im_bg_message_mirror_left));
            }
            msgTvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.im_message_mirror_left_text));
        } else {
            if (msgTvName != null) {
                msgTvName.setTextColor(ContextCompat.getColor(getContext(), R.color.im_message_txt_receive));
            }
            msgTvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.common_text_dark_light));
        }
    }

    @Override
    public void onRelease() {
        super.onRelease();
        if (showHeader) {
            mImageLoader.clear(getContext(), ImageConfigImpl.builder()
                    .imageView(msgIvPortrait)
                    .build());
        }
        mImageLoader.clear(getContext(), ImageConfigImpl.builder()
                .imageView(msgIvImage)
                .build());
    }
}