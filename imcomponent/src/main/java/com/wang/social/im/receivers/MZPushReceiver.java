package com.wang.social.im.receivers;

import android.content.Context;
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

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-19 15:40
 * ============================================
 */
public class MZPushReceiver extends MzPushMessageReceiver {
    @Override
    public void onRegister(Context context, String s) {

    }

    @Override
    public void onUnRegister(Context context, boolean b) {

    }

    @Override
    public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {

    }

    /**
     * 订阅状态，获取 PushId
     *
     * @param context
     * @param registerStatus
     */
    @Override
    public void onRegisterStatus(Context context, RegisterStatus registerStatus) {
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

    /**
     * 反订阅回调
     */
    @Override
    public void onUnRegisterStatus(Context context, UnRegisterStatus unRegisterStatus) {

    }

    /**
     * 标签状态回调
     */
    @Override
    public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {

    }

    /**
     * 别名状态回调
     */
    @Override
    public void onSubAliasStatus(Context context, SubAliasStatus subAliasStatus) {

    }

    @Override
    public void onNotificationClicked(Context context, MzPushMessage mzPushMessage) {
        // 消息正文内容
        String content = mzPushMessage.getContent();
        // 消息扩展内容
        String ext = mzPushMessage.getSelfDefineContentString();
        Log.i(TAG, "onNotificationClicked content: " + content + "|selfDefined ext: " + ext);
    }
}
