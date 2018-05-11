package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
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

    @BindView(R2.id.msg_tv_time)
    TextView msgTvTime;
    @BindView(R2.id.msg_iv_portrait)
    ImageView msgIvPortrait;
    @BindView(R2.id.msg_tv_name)
    @Nullable
    TextView msgTvName;
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

    }

    @Override
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        if (itemValue.isShowTime()) {
            msgTvTime.setVisibility(View.VISIBLE);
            msgTvTime.setText(getTimeStr(itemValue.getTimMessage().timestamp()));
        } else {
            msgTvTime.setVisibility(View.GONE);
        }

        if (showNickname && msgTvName != null) {
            msgTvName.setVisibility(View.VISIBLE);
            msgTvName.setText(itemValue.getNickname(conversationType));
        } else if (msgTvName != null) {
            msgTvName.setVisibility(View.GONE);
        }

        //头像
        if (showHeader) {
            msgIvPortrait.setVisibility(View.VISIBLE);
            mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                    .placeholder(R.drawable.common_default_circle_placeholder)
                    .imageView(msgIvPortrait)
                    .isCircle(true)
                    .url(itemValue.getPortrait(conversationType))
                    .build());
        } else {
            msgIvPortrait.setVisibility(View.GONE);
        }

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
