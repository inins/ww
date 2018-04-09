package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.http.imageloader.glide.RoundedCornersTransformation;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMImage;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.wang.social.im.R;
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

    @BindView(R.id.msg_tv_time)
    TextView msgTvTime;
    @BindView(R.id.msg_iv_portrait)
    ImageView msgIvPortrait;
    @BindView(R.id.msg_tv_name)
    TextView msgTvName;
    @BindView(R.id.msg_iv_image)
    ImageView msgIvImage;

    public ImageViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
    }

    @Override
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        if (showTimestamp) {
            msgTvTime.setVisibility(View.VISIBLE);
            msgTvTime.setText(getTimeStr(itemValue.getTimMessage().timestamp()));
        } else {
            msgTvTime.setVisibility(View.GONE);
        }

        TIMUserProfile profile = itemValue.getTimMessage().getSenderProfile();
        String faceUrl;
        if (itemValue.getTimMessage().isSelf()) {
            faceUrl = getSelfFaceUrl();
        } else {
            if (showNickname) {
                msgTvName.setVisibility(View.VISIBLE);
                if (profile != null) {
                    msgTvName.setText(profile.getRemark());
                    faceUrl = profile.getFaceUrl();
                } else {
                    msgTvName.setText("");
                    faceUrl = "";
                }
            } else {
                msgTvName.setVisibility(View.GONE);
                if (profile != null) {
                    faceUrl = profile.getFaceUrl();
                } else {
                    faceUrl = "";
                }
            }
        }
        //头像
        mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                .placeholder(R.drawable.common_default_circle_placeholder)
                .imageView(msgIvPortrait)
                .isCircle(true)
                .url(faceUrl)
                .build());
        //图片
        TIMMessage timMessage = itemValue.getTimMessage();
        for (int i = 0, max = (int) timMessage.getElementCount(); i < max; i++) {
            TIMElem elem = timMessage.getElement(i);
            if (elem instanceof TIMImageElem) {
                TIMImageElem imageElem = (TIMImageElem) elem;
                ArrayList<TIMImage> images = imageElem.getImageList();
                if (images.size() > 0) {
                    mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                            .imageView(msgIvImage)
                            .url(images.get(0).getUrl())
                            .placeholder(R.drawable.common_default_circle_placeholder)
                            .transformation(new RoundedCornersTransformation(getContext().getResources().getDimensionPixelOffset(R.dimen.im_msg_image_radius), 0))
                            .build());
                    break;
                }
            }
        }
    }

    @Override
    public void onRelease() {
        super.onRelease();
        mImageLoader.clear(getContext(), ImageConfigImpl.builder()
                .imageView(msgIvPortrait)
                .isClearMemory(true)
                .build());
        mImageLoader.clear(getContext(), ImageConfigImpl.builder()
                .imageView(msgIvImage)
                .isClearMemory(true)
                .build());
    }
}