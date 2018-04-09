package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.frame.base.BaseViewHolder;
import com.frame.http.imageloader.ImageLoader;
import com.frame.utils.FrameUtils;
import com.frame.utils.ScreenUtils;
import com.frame.utils.TimeUtils;
import com.tencent.imsdk.TIMConversationType;
import com.wang.social.im.enums.ConversationType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * ==========================================
 * 消息{@link android.support.v7.widget.RecyclerView.ViewHolder}基类
 * <p>
 * Create by ChenJing on 2018-04-02 19:33
 * ==========================================
 */
public abstract class BaseMessageViewHolder<T> extends BaseViewHolder<T>{

    ImageLoader mImageLoader;

    public ConversationType conversationType;
    public boolean showTimestamp = true;
    public boolean showNickname;

    public BaseMessageViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, null, layoutRes);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ScreenUtils.getScreenWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(lp);

        mImageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
    }

    protected String getTimeStr(long timestamp){
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
        if (messageYear != currentYear || messageMonth != currentMonth || messageDay != currentDay){
            return TimeUtils.millis2String(timestamp);
        }else {
            return TimeUtils.millis2String(timestamp, new SimpleDateFormat("HH:mm:ss", Locale.getDefault()));
        }
    }

    protected String getSelfFaceUrl(){
        return "http://resouce.dongdongwedding.com/2017-08-08_rtUbDxhH.png";
    }
}
