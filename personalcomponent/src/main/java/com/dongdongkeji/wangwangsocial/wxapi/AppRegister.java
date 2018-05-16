package com.dongdongkeji.wangwangsocial.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wang.social.personal.utils.PayUtil;
import com.wang.social.socialize.SocializeUtil;

import timber.log.Timber;

/**
 * Created by Administrator on 2017/8/31.
 */

public class AppRegister extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.e("OnWXregisterApp");
        IWXAPI api = WXAPIFactory.createWXAPI(context, null);
        api.registerApp(SocializeUtil.WX_APP_ID);
    }
}
