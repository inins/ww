package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frame.base.BaseViewHolder;
import com.frame.http.imageloader.ImageLoader;
import com.frame.utils.FrameUtils;
import com.frame.utils.ScreenUtils;
import com.frame.utils.TimeUtils;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessageStatus;
import com.wang.social.im.enums.ConversationType;
import com.wang.social.im.mvp.model.entities.UIMessage;

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
public abstract class BaseMessageViewHolder<T> extends BaseViewHolder<T> {

    ImageLoader mImageLoader;

    public ConversationType conversationType;
    public boolean showNickname;
    public boolean showHeader;
    public OnHandleListener mHandleListener;

    public BaseMessageViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, null, layoutRes);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ScreenUtils.getScreenWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(lp);

        mImageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
    }

    @Override
    public void onRelease() {
        super.onRelease();
        conversationType = null;
        mHandleListener = null;
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
            if (messageYear != currentYear){
                return TimeUtils.millis2String(timestamp, new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()));
            }else {
                return TimeUtils.millis2String(timestamp, new SimpleDateFormat("MM.dd HH:mm", Locale.getDefault()));
            }
        } else {
            return TimeUtils.millis2String(timestamp, new SimpleDateFormat("HH:mm", Locale.getDefault()));
        }
    }

    /**
     * 设置消息发送状态
     *
     * @param uiMessage
     * @param ivError
     * @param progress
     */
    protected void showStatus(UIMessage uiMessage, ImageView ivError, ContentLoadingProgressBar progress) {
        if (ivError == null || progress == null) {
            return;
        }
        switch (uiMessage.getTimMessage().status()) {
            case Sending:
                ivError.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                break;
            case SendFail:
                ivError.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                break;
            case SendSucc:
            default:
                ivError.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 设置头像点击事件
     * @param ivPortrait
     * @param uiMessage
     * @param position
     */
    protected void setPortraitListener(ImageView ivPortrait, UIMessage uiMessage, int position){
//        ivPortrait.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mHandleListener != null){
//                    mHandleListener.onPortraitClick(v, uiMessage, position);
//                }
//            }
//        });
        ivPortrait.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mHandleListener != null){
                    mHandleListener.onPortraitLongClick(v, uiMessage, position);
                }
                return false;
            }
        });
    }

    /**
     * 设置内容点击事件
     * @param view
     * @param uiMessage
     * @param position
     * @param click
     * @param longClick
     */
    protected void setContentListener(View view, UIMessage uiMessage, int position, boolean click, boolean longClick){
        if (click){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHandleListener != null){
                        mHandleListener.onContentClick(v, uiMessage, position);
                    }
                }
            });
        }
        if (longClick){
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mHandleListener != null){
                        mHandleListener.onContentLongClick(v, uiMessage, position);
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 发送失败图标点击事件
     * @param ivError
     * @param uiMessage
     * @param position
     */
    protected void setErrorListener(ImageView ivError, UIMessage uiMessage, int position){
        ivError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHandleListener != null){
                    mHandleListener.onErrorClick(position, uiMessage);
                }
            }
        });
    }

    /**
     * 初始化页面样式
     * @param uiMessage
     */
    protected abstract void initStyle(UIMessage uiMessage);

    public interface OnHandleListener {

        /**
         * 发送失败图标点击
         *
         * @param position
         * @param uiMessage
         */
        void onErrorClick(int position, UIMessage uiMessage);

        /**
         * 内容点击
         *
         * @param view
         * @param uiMessage
         * @param position
         */
        void onContentClick(View view, UIMessage uiMessage, int position);

        /**
         * 消息内容长按
         *
         * @param view
         * @param uiMessage
         * @param position
         */
        void onContentLongClick(View view, UIMessage uiMessage, int position);

        /**
         * 头像点击
         *
         * @param view
         * @param uiMessage
         * @param position
         */
        void onPortraitClick(View view, UIMessage uiMessage, int position);

        /**
         * 头像长按
         *
         * @param view
         * @param uiMessage
         * @param position
         */
        void onPortraitLongClick(View view, UIMessage uiMessage, int position);
    }
}