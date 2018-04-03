package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.view.ViewGroup;

import com.frame.base.BaseViewHolder;
import com.frame.http.imageloader.ImageLoader;
import com.frame.utils.TimeUtils;
import com.tencent.imsdk.TIMConversationType;

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

    @Inject
    ImageLoader mImageLoader;

    public TIMConversationType conversationType;
    public boolean showTimestamp = true;

    public BaseMessageViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
    }

    protected String getTimeStr(long timestamp){
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
}
