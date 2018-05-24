package com.wang.social.im.app;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.frame.utils.FrameUtils;
import com.google.gson.Gson;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.wang.social.im.R;
import com.wang.social.im.enums.CustomElemType;
import com.wang.social.im.mvp.model.entities.notify.DynamicMessage;
import com.wang.social.im.mvp.model.entities.notify.SystemMessage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Observable;

/**
 * ============================================
 * 全局消息监听
 * 服务器推送消息监听
 * <p>
 * Create by ChenJing on 2018-05-23 11:21
 * ============================================
 */
public class GlobalMessageEvent extends Observable implements TIMMessageListener {

    private static volatile GlobalMessageEvent mInstance;
    private Application mApplication;
    private Gson mGson;

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

    /**
     * 在通知栏中提醒:系统消息
     *
     * @param message
     */
    private void showSystemNotify(SystemMessage message) {
        NotificationCompat.Builder builder = getBuilder(message.getTitle(), message.getPushContent());
        Intent intent = mApplication.getApplicationContext().getPackageManager().getLaunchIntentForPackage(mApplication.getPackageName());
        builder.setContentIntent(PendingIntent.getActivity(mApplication, (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT));
        ((NotificationManager) mApplication.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE)).notify(IMConstants.SERVER_PUSH_MESSAGE_ACCOUNT, 520, builder.build());
    }

    /**
     * 在通知栏中提醒:动态消息
     *
     * @param message
     */
    private void showDynamicNotify(DynamicMessage message) {
        NotificationCompat.Builder builder = getBuilder(null, message.getPushContent());
        Intent intent = mApplication.getApplicationContext().getPackageManager().getLaunchIntentForPackage(mApplication.getPackageName());
        builder.setContentIntent(PendingIntent.getActivity(mApplication, (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT));
        ((NotificationManager) mApplication.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE)).notify(IMConstants.SERVER_PUSH_MESSAGE_ACCOUNT, 520, builder.build());
    }

    private NotificationCompat.Builder getBuilder(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mApplication.getBaseContext(), null);
        if (!TextUtils.isEmpty(title)) {
            builder.setContentTitle(title);
        }
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.im_luncher);
        builder.setAutoCancel(true);
        builder.setTicker(content);
        return builder;
    }
}