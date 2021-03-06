package com.wang.social.im.app;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.frame.component.common.AppConstant;
import com.frame.component.enums.ConversationType;
import com.frame.component.utils.viewutils.AppUtil;
import com.frame.di.component.AppComponent;
import com.frame.utils.AppUtils;
import com.frame.utils.FrameUtils;
import com.google.gson.Gson;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMOfflinePushSettings;
import com.tencent.imsdk.TIMValueCallBack;
import com.wang.social.im.R;
import com.wang.social.im.enums.CustomElemType;
import com.frame.component.entities.DynamicMessage;
import com.frame.component.entities.SystemMessage;
import com.wang.social.im.enums.MessageType;
import com.wang.social.im.helper.ImHelper;
import com.wang.social.im.mvp.model.entities.UIConversation;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.utils.badge.ShortcutBadger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Observable;

import static com.wang.social.im.app.IMConstants.SERVER_PUSH_MESSAGE_ACCOUNT;

/**
 * ============================================
 * 全局消息监听
 * 服务器推送消息监听
 * <p>
 * Create by ChenJing on 2018-05-23 11:21
 * ============================================
 */
public class GlobalMessageEvent extends Observable implements TIMMessageListener {

    private static final String NOTIFY_CHANNEL_ID = "10";

    private static volatile GlobalMessageEvent mInstance;
    private Application mApplication;
    private Gson mGson;
    private TIMOfflinePushSettings mPushSettings;

    private GlobalMessageEvent() {
    }

    public static GlobalMessageEvent getInstance() {
        if (mInstance == null) {
            synchronized (GlobalMessageEvent.class) {
                if (mInstance == null) {
                    mInstance = new GlobalMessageEvent();
                }
            }
        }
        return mInstance;
    }

    public void init(Application application) {
        this.mApplication = application;
        mGson = FrameUtils.obtainAppComponentFromContext(application).gson();

        TIMManager.getInstance().addMessageListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFY_CHANNEL_ID, "往往", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) mApplication.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }

    public void setOfflineSetting(TIMOfflinePushSettings offlineSetting) {
        if (offlineSetting != null) {
            mPushSettings = offlineSetting;
            return;
        }
        TIMManager.getInstance().getOfflinePushSettings(new TIMValueCallBack<TIMOfflinePushSettings>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(TIMOfflinePushSettings timOfflinePushSettings) {
                mPushSettings = timOfflinePushSettings;
            }
        });
    }

    /**
     * 监听新消息
     *
     * @param list
     * @return
     */
    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        if (list != null) {
            for (TIMMessage message : list) {
                if (message.getSender().equals(IMConstants.SERVER_PUSH_MESSAGE_ACCOUNT)) { //检测此条消息是否为服务器的推送消息
                    //解析推送消息内容
                    formatMessage(message);
                } else {
                    UIConversation conversation = new UIConversation(message.getConversation());
                    if ((conversation.getConversationType() == ConversationType.PRIVATE && !conversation.getIdentify().equals(IMConstants.SERVER_PUSH_MESSAGE_ACCOUNT)) ||
                            conversation.getConversationType() == ConversationType.SOCIAL ||
                            conversation.getConversationType() == ConversationType.TEAM) {
                        showMessageNotify(message);
                    }
                }
            }
        }
        return false;
    }

    /**
     * 解析推送消息
     *
     * @param message
     */
    private void formatMessage(TIMMessage message) {
        int elemCount = (int) message.getElementCount();
        if (elemCount > 0) {
            TIMElem elem = message.getElement(0);
            if (elem.getType() == TIMElemType.Custom) {
                CustomElemType elemType = CustomElemType.getElemType((TIMCustomElem) elem);
                byte[] dataBytes = ((TIMCustomElem) elem).getData();
                if (elemType != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(dataBytes));
                        String content = jsonObject.getString("msgContent");
                        switch (elemType) {
                            case SYSTEM_NOTIFY://系统消息
                                SystemMessage systemMessage = mGson.fromJson(content, SystemMessage.class);
                                showSystemNotify(systemMessage);
                                EventBus.getDefault().post(systemMessage);
                                break;
                            case DYNAMIC_NOTIFY://动态消息
                                DynamicMessage dynamicMessage = mGson.fromJson(content, DynamicMessage.class);
                                showDynamicNotify(dynamicMessage);
                                EventBus.getDefault().post(dynamicMessage);
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void showMessageNotify(TIMMessage timMessage) {
        if (timMessage.getRecvFlag() == TIMGroupReceiveMessageOpt.ReceiveNotNotify ||
                !isOfflinePush() || UIMessage.obtain(timMessage).getMessageType() == MessageType.UNKNOWN) {
            return;
        }
        if (AppUtils.isAppForeground()) {
            ImHelper.playMessageNotify(mApplication);
            return;
        }
        //显示角标
        ShortcutBadger.applyCount(mApplication, ImHelper.getTotalUnreadCount());
        //显示通知
        UIMessage uiMessage = UIMessage.obtain(timMessage);
        NotificationCompat.Builder builder = getBuilder("新消息", uiMessage.getSummary());
        Intent intent = buildIntent(timMessage);
        builder.setContentIntent(PendingIntent.getActivity(mApplication, (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT));
        ((NotificationManager) mApplication.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE)).notify(IMConstants.SERVER_PUSH_MESSAGE_ACCOUNT, 521, builder.build());
    }

    /**
     * 在通知栏中提醒:系统消息
     *
     * @param message
     */
    private void showSystemNotify(SystemMessage message) {
        if (!isOfflinePush()) {
            return;
        }
        NotificationCompat.Builder builder = getBuilder(message.getTitle(), TextUtils.isEmpty(message.getPushContent()) ? message.getContent() : message.getPushContent());
//        Intent intent = mApplication.getApplicationContext().getPackageManager().getLaunchIntentForPackage(mApplication.getPackageName());
        Intent intent = buildIntent(message);
        builder.setContentIntent(PendingIntent.getActivity(mApplication, (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT));
        ((NotificationManager) mApplication.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE)).notify(IMConstants.SERVER_PUSH_MESSAGE_ACCOUNT, 520, builder.build());
    }

    /**
     * 在通知栏中提醒:动态消息
     *
     * @param message
     */
    private void showDynamicNotify(DynamicMessage message) {
        if (!isOfflinePush()) {
            return;
        }
        NotificationCompat.Builder builder = getBuilder(null, TextUtils.isEmpty(message.getPushContent()) ? message.getMsgContent() : message.getPushContent());
//        Intent intent = mApplication.getApplicationContext().getPackageManager().getLaunchIntentForPackage(mApplication.getPackageName());
        Intent intent = buildIntent(message);
        builder.setContentIntent(PendingIntent.getActivity(mApplication, (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT));
        ((NotificationManager) mApplication.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE)).notify(IMConstants.SERVER_PUSH_MESSAGE_ACCOUNT, 520, builder.build());
    }

    private NotificationCompat.Builder getBuilder(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mApplication.getBaseContext(), null);
        if (!TextUtils.isEmpty(title)) {
            builder.setContentTitle(title);
        }
        builder.setContentText(content)
                .setSmallIcon(R.drawable.im_ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(mApplication.getResources(), R.drawable.im_luncher))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setChannelId(NOTIFY_CHANNEL_ID)
                .setTicker(content);
        return builder;
    }

    private Intent buildIntent(Object msg) {
        Uri.Builder uriBuilder = Uri.parse("wang://" + mApplication.getApplicationInfo().processName).buildUpon()
                .appendPath("wangwang");
        if (msg instanceof SystemMessage) {
            SystemMessage message = (SystemMessage) msg;
            switch (message.getType()) {
                case SystemMessage.TYPE_ADD_FRIEND:
                    uriBuilder.appendQueryParameter("target", AppConstant.Key.OPEN_TARGET_FRIEND_APPLY)
                            .appendQueryParameter("targetId", "-1");
                    break;
                case SystemMessage.TYPE_GROUP_APPLY:
                    uriBuilder.appendQueryParameter("target", AppConstant.Key.OPEN_TARGET_GROUP_APPLY)
                            .appendQueryParameter("targetId", "-1");
                    break;
                case SystemMessage.TYPE_GROUP_INVITE:
                    uriBuilder.appendQueryParameter("target", AppConstant.Key.OPEN_TARGET_GROUP_INVITE)
                            .appendQueryParameter("targetId", "-1");
                    break;
                default:
                    uriBuilder.appendQueryParameter("target", AppConstant.Key.OPEN_TARGET_SYS_MESSAGE)
                            .appendQueryParameter("targetId", "-1");
                    break;
            }
        } else if (msg instanceof DynamicMessage) {
            DynamicMessage message = (DynamicMessage) msg;
            switch (message.getModeType()) {
                case DynamicMessage.TYPE_PRAISE_FUN_SHOW:
                case DynamicMessage.TYPE_PRAISE_FUN_SHOW_COMMENT:
                case DynamicMessage.TYPE_PRAISE_TOPIC:
                case DynamicMessage.TYPE_PRAISE_TOPIC_COMMENT:
                    uriBuilder.appendQueryParameter("target", AppConstant.Key.OPEN_TARGET_PRAISE)
                            .appendQueryParameter("targetId", "-1");
                    break;
                case DynamicMessage.TYPE_REPLY_FUN_SHOW_COMMENT:
                case DynamicMessage.TYPE_COMMENT_FUN_SHOW:
                case DynamicMessage.TYPE_COMMENT_TOPIC:
                case DynamicMessage.TYPE_REPLY_TOPIC_COMMENT:
                    uriBuilder.appendQueryParameter("target", AppConstant.Key.OPEN_TARGET_COMMENT)
                            .appendQueryParameter("targetId", "-1");
                    break;
                case DynamicMessage.TYPE_REPLY_FUN_SHOW_AITE:
                    uriBuilder.appendQueryParameter("target", AppConstant.Key.OPEN_TARGET_ALERT)
                            .appendQueryParameter("targetId", "-1");
                    break;
            }
        } else if (msg instanceof TIMMessage) {
            TIMMessage message = (TIMMessage) msg;
            TIMConversation conversation = message.getConversation();
            if (conversation.getPeer().startsWith(IMConstants.IM_IDENTITY_PREFIX_TEAM)) {
                uriBuilder.appendQueryParameter("target", AppConstant.Key.OPEN_TARGET_TEAM);
            } else if (conversation.getPeer().startsWith(IMConstants.IM_IDENTITY_PREFIX_SOCIAL)) {
                uriBuilder.appendQueryParameter("target", AppConstant.Key.OPEN_TARGET_SOCIAL);
            } else if (conversation.getType() == TIMConversationType.C2C) {
                uriBuilder.appendQueryParameter("target", AppConstant.Key.OPEN_TARGET_C2C);
            }
            uriBuilder.appendQueryParameter("targetId", conversation.getPeer());
        }
        return new Intent("android.intent.action.VIEW", uriBuilder.build());
    }

    private boolean isOfflinePush() {
        if (mPushSettings != null && mPushSettings.isEnabled()) {
            return true;
        }
        return false;
    }
}