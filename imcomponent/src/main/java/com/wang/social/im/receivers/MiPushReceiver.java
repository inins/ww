package com.wang.social.im.receivers;

import android.content.Context;

import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-19 13:11
 * ============================================
 */
public class MiPushReceiver extends PushMessageReceiver {

    /**
     * 接收服務器向客戶端發送的透傳消息
     *
     * @param context
     * @param miPushMessage
     */
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        super.onReceivePassThroughMessage(context, miPushMessage);
    }

    /**
     * 接收服务器向客户端发送的通知消息
     * 这个回调方法是在通知消息到达客户端时触发，另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数
     *
     * @param context
     * @param miPushMessage
     */
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageArrived(context, miPushMessage);
    }

    /**
     * 用来接收服务器向客户端发送的通知消息
     * 这个回调方法会在用户手动点击通知后触发
     *
     * @param context
     * @param miPushMessage
     */
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageClicked(context, miPushMessage);
    }

    /**
     * 用来接收客户端向服务器发送命令后的响应结果
     *
     * @param context
     * @param miPushCommandMessage
     */
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage);
    }

    /**
     * 用来接收客户端向服务器发送注册命令后的响应结果
     *
     * @param context
     * @param miPushCommandMessage
     */
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onReceiveRegisterResult(context, miPushCommandMessage);
    }
}