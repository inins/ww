package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.enums.ConversationType;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.http.imageloader.glide.RoundedCornersTransformation;
import com.frame.utils.FrameUtils;
import com.google.gson.Gson;
import com.tencent.imsdk.TIMLocationElem;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.LocationAddressInfo;
import com.wang.social.im.mvp.model.entities.UIMessage;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-26 18:37
 * ============================================
 */
public class LocationViewHolder extends BaseMessageViewHolder<UIMessage> {

    @BindView(R2.id.msg_tv_place)
    TextView msgTvPlace;
    @BindView(R2.id.msg_tv_address)
    TextView msgTvAddress;
    @BindView(R2.id.msg_iv_location)
    ImageView msgIvLocation;
    @BindView(R2.id.msg_cl_location)
    FrameLayout msgClLocation;
    @BindView(R2.id.msg_iv_error)
    @Nullable
    ImageView msgIvError;
    @BindView(R2.id.msg_pb_progress)
    @Nullable
    ContentLoadingProgressBar msgPbProgress;

    Gson gson;

    public LocationViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);

        gson = FrameUtils.obtainAppComponentFromContext(context).gson();
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
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        initStyle(itemValue);

        super.bindData(itemValue, position, onItemClickListener);

        TIMLocationElem locationElem = (TIMLocationElem) itemValue.getMessageElem(TIMLocationElem.class);
        if (locationElem != null) {
            try {
                LocationAddressInfo addressInfo = gson.fromJson(locationElem.getDesc(), LocationAddressInfo.class);
                msgTvPlace.setText(addressInfo.getPlace());
                msgTvAddress.setText(addressInfo.getAddress());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                    .imageView(msgIvLocation)
                    .url(getLocationUrl(locationElem.getLatitude(), locationElem.getLongitude()))
                    .errorPic(R.drawable.im_image_message_placeholder)
                    .placeholder(R.drawable.im_image_message_placeholder)
                    .transformation(new RoundedCornersTransformation(getContext().getResources().getDimensionPixelSize(R.dimen.im_msg_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                    .build());
        }

        if (itemValue.getTimMessage().isSelf()) {
            showStatus(itemValue, msgIvError, msgPbProgress);
            setErrorListener(msgIvError, itemValue, position);
        } else {
            setPortraitListener(msgIvPortrait, itemValue, position);
        }
        setContentListener(msgClLocation, itemValue, position, true, true);
    }

    @Override
    public void onRelease() {
        super.onRelease();
        gson = null;
    }

    private String getLocationUrl(double latitude, double longitude) {
        return "http://restapi.amap.com/v3/staticmap?location=" + longitude + "," + latitude + "&zoom=16&scale=2&size=408*240&markers=mid,,A:" + longitude + "," + latitude + "&key=e09af6a2b26c02086e9216bd07c960ae";
    }
}
