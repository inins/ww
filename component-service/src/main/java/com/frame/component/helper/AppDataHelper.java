package com.frame.component.helper;

import com.frame.component.entities.User;
import com.frame.component.entities.config.MsgConfig;
import com.frame.component.entities.location.LocationInfo;
import com.frame.component.entities.msg.NotifySave;
import com.frame.utils.SPUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/4/3.
 */

public class AppDataHelper {

    private static final String SHARENAME = "app_config";
    private static final String KEY_USER = "user";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_IM_SIGN = "sign";
    private static final String KEY_MSGCONFIG = "msg_config";
    private static final String KEY_NOTIFY_SAVE = "notify_save";
    private static final String KEY_SOCIAL_TIP_ANIM = "social_anim";
    private static final String KEY_LOCATION_INFO = "location_info";

    /////////////////// 用户对象持久化 ///////////////////

    public static void saveUser(User user) {
        SPUtils.getInstance(SHARENAME).put(KEY_USER, user);
    }

    public static User getUser() {
        return (User) SPUtils.getInstance(SHARENAME).get(KEY_USER);
    }

    public static void removeUser() {
        SPUtils.getInstance(SHARENAME).remove(KEY_USER);
    }

    /////////////////// token/sign持久化 ///////////////////

    public static void saveToken(String token) {
        SPUtils.getInstance(SHARENAME).put(KEY_TOKEN, token);
    }

    public static void saveImSign(String sign) {
        SPUtils.getInstance(SHARENAME).put(KEY_IM_SIGN, sign);
    }

    public static String getToken() {
        return SPUtils.getInstance(SHARENAME).getString(KEY_TOKEN);
    }

    public static String getSign() {
        return SPUtils.getInstance(SHARENAME).getString(KEY_IM_SIGN);
    }

    public static void removeToken() {
        SPUtils.getInstance(SHARENAME).remove(KEY_TOKEN);
        SPUtils.getInstance(SHARENAME).remove(KEY_IM_SIGN);
    }

    /////////////////// msgSetting持久化 ///////////////////

    public static void saveMsgConfig(MsgConfig msgConfig) {
        SPUtils.getInstance(SHARENAME).put(KEY_MSGCONFIG, msgConfig);
    }

    public static MsgConfig getMsgConfig() {
        MsgConfig msgConfig = (MsgConfig) SPUtils.getInstance(SHARENAME).get(KEY_MSGCONFIG);
        if (msgConfig == null) return new MsgConfig();
        else return msgConfig;
    }

    public static void removeMsgConfig() {
        SPUtils.getInstance(SHARENAME).remove(KEY_MSGCONFIG);
    }

    /////////////////// 通知消息持久化 ///////////////////

    public static void saveNotifySave(NotifySave notifySave) {
        SPUtils.getInstance(SHARENAME).put(KEY_NOTIFY_SAVE, notifySave);
    }

    public static NotifySave getNotifySave() {
        NotifySave notifySave = (NotifySave) SPUtils.getInstance(SHARENAME).get(KEY_NOTIFY_SAVE);
        if (notifySave == null) return new NotifySave();
        else return notifySave;
    }

    ////////////////// 趣聊提示动画状态 /////////////////
    public static void saveTipShowStatus(boolean isShowed) {
        SPUtils.getInstance(SHARENAME).put(KEY_SOCIAL_TIP_ANIM, isShowed);
    }

    public static boolean isTipShowed() {
        return SPUtils.getInstance(SHARENAME).getBoolean(KEY_SOCIAL_TIP_ANIM, false);
    }

    public static void removeNotifySave() {
        SPUtils.getInstance(SHARENAME).remove(KEY_NOTIFY_SAVE);
    }

    /////////////////// 位置信息持久化 ///////////////////

    public static void saveLocationInfo(LocationInfo locationInfo) {
        SPUtils.getInstance(SHARENAME).put(KEY_LOCATION_INFO, locationInfo);
    }

    public static LocationInfo getLocationInfo() {
        return (LocationInfo) SPUtils.getInstance(SHARENAME).get(KEY_LOCATION_INFO);
    }

}
