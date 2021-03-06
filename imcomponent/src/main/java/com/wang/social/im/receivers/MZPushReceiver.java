package com.wang.social.im.receivers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.handler.MzPushMessage;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushToken;
import com.wang.social.im.app.IMConstants;

import timber.log.Timber;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-19 15:40
 * ============================================
 */
public class MZPushReceiver extends MzPushMessageReceiver {

    private static final String TAG = MZPushReceiver.class.getSimpleName();

    @Override
    public void onRegister(Context context, String s) {
        Timber.tag(TAG).d("onRegister:" + s);
    }

    @Override
    public void onUnRegister(Context context, boolean b) {
        Timber.tag(TAG).d("onUnRegister:");
    }

    @Override
    public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {
        Timber.tag(TAG).d("onPushStatus:" + pushSwitchStatus.toString());
    }

    @Override
    public void onRegisterStatus(Context context, RegisterStatus registerStatus) {
        Timber.tag(TAG).d("onRegisterStatus:" + registerStatus.getCode() + "," +registerStatus.getPushId());
        //上报 busiid 和 pushid 到腾讯云，需要在登录成功后进行上报
        TIMOfflinePushToken token = new TIMOfflinePushToken(IMConstants.MZPUSH_BUSSID, registerStatus.getPushId());
        TIMManager.getInstance().setOfflinePushToken(token, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "setOfflinePushToken failed, code: " + i + "|msg: " + s);
            }

            @Override
            public void onSuccess() {
                Log.i(TAG, "setOfflinePushToken succ");
            }
        });
    }

    @Override
    public void onUnRegisterStatus(Context context, UnRegisterStatus unRegisterStatus) {
        Timber.tag(TAG).d("onUnRegisterStatus:");
    }

    @Override
    public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {

    }

    @Override
    public void onSubAliasStatus(Context context, SubAliasStatus subAliasStatus) {

    }

    @Override
    public void onNotificationArrived(Context context, MzPushMessage mzPushMessage) {
        super.onNotificationArrived(context, mzPushMessage);
        Timber.tag(TAG).d("onNotificationArrived:"+mzPushMessage.getContent());
    }

    @Override
    public void onNotifyMessageArrived(Context context, String s) {
        super.onNotifyMessageArrived(context, s);
        Timber.tag(TAG).d("onNotifyMessageArrived:"+s);
    }

    @Override
    public void onNotificationClicked(Context context, MzPushMessage mzPushMessage) {
        super.onNotificationClicked(context, mzPushMessage);
        Timber.tag(TAG).d("onNotificationClicked:"+mzPushMessage.getContent());
    }

    @Override
    public void onMessage(Context context, String s) {
        super.onMessage(context, s);
        Timber.tag(TAG).d("onMessage:"+s);
    }

    @Override
    public void onMessage(Context context, Intent intent) {
        super.onMessage(context, intent);
        Timber.tag(TAG).d("onMessage:intent");
    }

    @Override
    public void onMessage(Context context, String s, String s1) {
        super.onMessage(context, s, s1);
        Timber.tag(TAG).d("onMessage:s1 "+s);
    }
}
