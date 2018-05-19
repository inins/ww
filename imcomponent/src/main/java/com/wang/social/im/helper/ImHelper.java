package com.wang.social.im.helper;

import android.app.Application;
import android.os.Build;
import android.text.TextUtils;

import com.frame.component.common.AppConstant;
import com.frame.component.enums.ConversationType;
import com.frame.utils.FileUtils;
import com.frame.utils.FrameUtils;
import com.frame.utils.Utils;
import com.huawei.android.hms.agent.HMSAgent;
import com.tencent.imsdk.TIMOfflinePushSettings;
import com.tencent.imsdk.TIMOfflinePushToken;
import com.wang.social.im.app.IMConstants;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-19 11:52
 * ============================================
 */
public class ImHelper {

    /**
     * 创建一个临时文件
     *
     * @param parent
     * @return
     */
    public static File getTempFile(String parent) {
        File cacheFile = new File(FrameUtils.obtainAppComponentFromContext(Utils.getContext()).cacheFile(), parent);
        FileUtils.createOrExistsDir(cacheFile);
        try {
            File tmpFile = File.createTempFile(parent, "", cacheFile);
            tmpFile.deleteOnExit();
            return tmpFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图片缓存地址
     *
     * @param fileName
     * @return
     */
    public static String getImageCachePath(String fileName) {
        File cacheFile = new File(FrameUtils.obtainAppComponentFromContext(Utils.getContext()).cacheFile(), AppConstant.Constant.IMAGE_CACHE_DIR);
        FileUtils.createOrExistsDir(cacheFile);
        if (TextUtils.isEmpty(fileName)) {
            return cacheFile.getPath();
        } else {
            return cacheFile.getPath() + "/" + fileName;
        }
    }

    /**
     * 判断缓存文件是否存在
     *
     * @param fileName
     * @return
     */
    public static boolean isCacheFileExit(String fileName) {
        File file = new File(getImageCachePath(fileName));
        return file.exists();
    }

    /**
     * 获取年龄段
     *
     * @param birthdayMills
     * @return
     */
    public static String getAgeRange(long birthdayMills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(birthdayMills);
        int year = calendar.get(Calendar.YEAR);
        if (year > 2000) {
            return "00后";
        } else if (year >= 1995 && year < 2000) {
            return "95后";
        } else if (year >= 1990 && year < 1995) {
            return "90后";
        } else {
            return "其他";
        }
    }

    /**
     * 将IM的ID转换成往往ID
     *
     * @param imId
     * @return
     */
    public static String imId2WangId(String imId) {
        return imId.replace(IMConstants.IM_IDENTITY_PREFIX_GAME, "")
                .replace(IMConstants.IM_IDENTITY_PREFIX_MIRROR, "")
                .replace(IMConstants.IM_IDENTITY_PREFIX_SOCIAL, "")
                .replace(IMConstants.IM_IDENTITY_PREFIX_TEAM, "");
    }

    /**
     * 往往ID转换成IM的ID
     *
     * @param wangId
     * @param type
     * @return
     */
    public static String wangId2ImId(String wangId, ConversationType type) {
        switch (type) {
            case GAME:
                return IMConstants.IM_IDENTITY_PREFIX_GAME + wangId;
            case MIRROR:
                return IMConstants.IM_IDENTITY_PREFIX_MIRROR + wangId;
            case TEAM:
                return IMConstants.IM_IDENTITY_PREFIX_TEAM + wangId;
            case SOCIAL:
                return IMConstants.IM_IDENTITY_PREFIX_SOCIAL + wangId;
            default:
                return wangId;
        }
    }

    /**
     * 配置离线推送
     */
    public static void configurationOfflinePush(Application application) {
        //登陆成功添加离线消息推送配置
        TIMOfflinePushSettings offlinePushSettings = new TIMOfflinePushSettings();
        offlinePushSettings.setEnabled(true);

        String vendor = Build.MANUFACTURER;
        //初始化推送
        if (vendor.contains("xiaomi")) { //小米推送
            MiPushClient.registerPush(application, IMConstants.MIPUSH_APPID, IMConstants.MIPUSH_APPKEY);
        } else if (vendor.contains("huawei")) {
            HMSAgent.init(application);
        }
    }
}
