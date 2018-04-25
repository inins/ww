package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.frame.utils.TimeUtils;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.EnvelopAdoptInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;

/**
 * ============================================
 * {@link com.wang.social.im.mvp.ui.adapters.EnvelopAdoptInfoAdapter}
 * <p>
 * Create by ChenJing on 2018-04-24 14:36
 * ============================================
 */
public class EnvelopAdoptViewHolder extends BaseViewHolder<EnvelopAdoptInfo> {

    @BindView(R.id.iea_iv_portrait)
    ImageView ieaIvPortrait;
    @BindView(R.id.iea_tv_nickname)
    TextView ieaTvNickname;
    @BindView(R.id.iea_tv_time)
    TextView ieaTvTime;
    @BindView(R.id.iea_tv_diamond)
    TextView ieaTvDiamond;
    @BindView(R.id.iea_tv_lucky)
    TextView ieaTvLucky;

    ImageLoader mImageLoader;

    public EnvelopAdoptViewHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.im_item_envelop_adopt);
        mImageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
    }

    @Override
    protected void bindData(EnvelopAdoptInfo itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        mImageLoader.loadImage(getContext(), ImageConfigImpl
                .builder()
                .url(itemValue.getPortrait())
                .imageView(ieaIvPortrait)
                .isCircle(true)
                .placeholder(R.drawable.common_default_circle_placeholder)
                .errorPic(R.drawable.common_default_circle_placeholder)
                .build());

        ieaTvNickname.setText(itemValue.getNickname());

        ieaTvTime.setText(getTimeStr(itemValue.getTime()));
        ieaTvDiamond.setText(String.valueOf(itemValue.getGotDiamondNumber()));
        if (itemValue.isLucky()) {
            ieaTvLucky.setVisibility(View.VISIBLE);
        } else {
            ieaTvLucky.setVisibility(View.GONE);
        }
    }

    protected String getTimeStr(long timestamp) {
        timestamp = timestamp * 1000;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(timestamp));
        int messageYear = cal.get(Calendar.YEAR);
        int messageMonth = cal.get(Calendar.MONTH);
        int messageDay = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(new Date());
        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        if (messageYear != currentYear || messageMonth != currentMonth || messageDay != currentDay) {
            if (messageYear != currentYear) {
                return TimeUtils.millis2String(timestamp, new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()));
            } else {
                return TimeUtils.millis2String(timestamp, new SimpleDateFormat("MM.dd HH:mm", Locale.getDefault()));
            }
        } else {
            return TimeUtils.millis2String(timestamp, new SimpleDateFormat("HH:mm", Locale.getDefault()));
        }
    }
}