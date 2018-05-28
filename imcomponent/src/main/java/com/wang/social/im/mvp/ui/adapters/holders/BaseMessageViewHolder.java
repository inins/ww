package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.frame.utils.ScreenUtils;
import com.frame.utils.TimeUtils;
import com.frame.component.enums.ConversationType;
import com.frame.utils.ToastUtil;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.mvp.model.entities.UIMessage;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import timber.log.Timber;

/**
 * ==========================================
 * 消息{@link android.support.v7.widget.RecyclerView.ViewHolder}基类
 * <p>
 * Create by ChenJing on 2018-04-02 19:33
 * ==========================================
 */
public abstract class BaseMessageViewHolder<T> extends BaseViewHolder<T> {

    @BindView(R2.id.msg_iv_portrait)
    @Nullable
    ImageView msgIvPortrait;
    @BindView(R2.id.msg_tv_time)
    @Nullable
    TextView msgTvTime;
    @BindView(R2.id.msg_tv_name)
    @Nullable
    TextView msgTvName;

    ImageLoader mImageLoader;

    public String targetId;
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
    protected void bindData(T itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        UIMessage message = (UIMessage) itemValue;
        if (message.isShowTime() && msgTvTime != null) {
            msgTvTime.setVisibility(View.VISIBLE);
            msgTvTime.setText(getTimeStr(message.getTimMessage().timestamp()));
        } else if (msgTvTime != null) {
            msgTvTime.setVisibility(View.GONE);
        }

        if (showNickname && msgTvName != null) {
            msgTvName.setVisibility(View.VISIBLE);
            msgTvName.setText(message.getNickname(conversationType));
        } else if (msgTvName != null) {
            msgTvName.setVisibility(View.GONE);
        }

        //头像
        if (showHeader && msgIvPortrait != null) {
            msgIvPortrait.setVisibility(View.VISIBLE);
            String portrait = message.getPortrait(conversationType);
            //处理群头像不能获取到的问题, 异步从服务器获取成员信息
            if (TextUtils.isEmpty(portrait) && (conversationType == ConversationType.SOCIAL || conversationType == ConversationType.TEAM)) { //如果取出的信息为空，并且会话类型为趣聊/觅聊,则异步从服务器获取信息
                TIMGroupManagerExt.getInstance().getGroupMembersInfo(targetId, Arrays.asList(message.getTimMessage().getSender()), new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
                    @Override
                    public void onError(int i, String s) {
                        Timber.d("查询群成员信息失败");
                    }

                    @Override
                    public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                        if (timGroupMemberInfos != null && !timGroupMemberInfos.isEmpty()) {
                            TIMGroupMemberInfo memberInfo = timGroupMemberInfos.get(0);
                            message.setNickname(memberInfo.getNameCard());
                            if (showNickname && msgTvName != null) {
                                msgTvName.setText(memberInfo.getNameCard());
                            }
                            byte[] portraitByte = memberInfo.getCustomInfo().get(IMConstants.IM_FIELD_GROUP_MEMBER_PORTRAIT);
                            String portraitStr = "";
                            if (portraitByte != null && portraitByte.length > 0) {
                                portraitStr = new String(portraitByte);
                                message.setPortrait(portraitStr);
                            }
                            mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                                    .placeholder(R.drawable.common_default_circle_placeholder)
                                    .errorPic(R.drawable.common_default_circle_placeholder)
                                    .imageView(msgIvPortrait)
                                    .isCircle(true)
                                    .url(portraitStr)
                                    .build());
                        }
                    }
                });
            } else {
                mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                        .placeholder(R.drawable.common_default_circle_placeholder)
                        .errorPic(R.drawable.common_default_circle_placeholder)
                        .imageView(msgIvPortrait)
                        .isCircle(true)
                        .url(portrait)
                        .build());
            }
        } else if (msgIvPortrait != null) {
            msgIvPortrait.setVisibility(View.GONE);
        }
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
            if (messageYear != currentYear) {
                return TimeUtils.millis2String(timestamp, new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()));
            } else {
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
     *
     * @param ivPortrait
     * @param uiMessage
     * @param position
     */
    protected void setPortraitListener(ImageView ivPortrait, UIMessage uiMessage, int position) {
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
                if (mHandleListener != null) {
                    mHandleListener.onPortraitLongClick(v, uiMessage, position);
                }
                return false;
            }
        });
    }

    /**
     * 设置内容点击事件
     *
     * @param view
     * @param uiMessage
     * @param position
     * @param click
     * @param longClick
     */
    protected void setContentListener(View view, UIMessage uiMessage, int position, boolean click, boolean longClick) {
        if (click) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHandleListener != null) {
                        mHandleListener.onContentClick(v, uiMessage, position);
                    }
                }
            });
        }
        if (longClick) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mHandleListener != null) {
                        mHandleListener.onContentLongClick(v, uiMessage, position);
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 发送失败图标点击事件
     *
     * @param ivError
     * @param uiMessage
     * @param position
     */
    protected void setErrorListener(ImageView ivError, UIMessage uiMessage, int position) {
        ivError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHandleListener != null) {
                    mHandleListener.onErrorClick(position, uiMessage);
                }
            }
        });
    }

    /**
     * 初始化页面样式
     *
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