package com.frame.component.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.frame.utils.Utils;

/**
 * Author: ChenJing
 * Date: 2017-09-25 下午 9:26
 * Version: 1.0
 */

public class ChannelUtils {

    public static int getChannelCode() {
        PackageManager packageManager = Utils.getContext().getPackageManager();
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(Utils.getContext().getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null && appInfo.metaData != null) {
                String channelName = appInfo.metaData.getString("UMENG_CHANNEL");

                if (channelName.equals("yingyongbao")) {
                    return 1;
                } else if (channelName.equals("_360")) {
                    return 2;
                } else if (channelName.equals("oppo")) {
                    return 8;
                } else if (channelName.equals("vivo")) {
                    return 7;
                } else if (channelName.equals("anzhi")) {
                    return 12;
                } else if (channelName.equals("baidu")) {
                    return 3;
                } else if (channelName.equals("huawei")) {
                    return 5;
                } else if (channelName.equals("lianxiang")) {
                    return 10;
                } else if (channelName.equals("wandoujia")) {
                    return 4;
                } else if (channelName.equals("xiaomi")) {
                    return 6;
                } else if (channelName.equals("sanxing")) {
                    return 9;
                } else if (channelName.equals("chuizi")) {
                    return 11;
                } else {
                    return 14;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 14;
    }
}
