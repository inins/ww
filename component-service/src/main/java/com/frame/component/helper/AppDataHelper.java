package com.frame.component.helper;

import com.frame.component.entities.User;
import com.frame.component.entities.config.MsgConfig;
import com.frame.utils.SPUtils;

/**
 * Created by Administrator on 2018/4/3.
 */

public class AppDataHelper {

    private static final String SHARENAME = "app_config";
    private static final String KEY_USER = "user";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_MSGCONFIG = "msg_config";

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

    /////////////////// token持久化 ///////////////////

    public static void saveToken(String token) {
        SPUtils.getInstance(SHARENAME).put(KEY_TOKEN, token);
    }

    public static String getToken() {
        return SPUtils.getInstance(SHARENAME).getString(KEY_TOKEN);
    }

    public static void removeToken() {
        SPUtils.getInstance(SHARENAME).remove(KEY_TOKEN);
    }

    /////////////////// msgSetting持久化 ///////////////////

    public static void saveMsgConfig(MsgConfig msgConfig) {
        SPUtils.getInstance(SHARENAME).put(KEY_MSGCONFIG, msgConfig);
    }

    public static MsgConfig getMsgConfig() {
        return (MsgConfig) SPUtils.getInstance(SHARENAME).get(KEY_MSGCONFIG);
    }

    public static void removeMsgConfig() {
        SPUtils.getInstance(SHARENAME).remove(KEY_MSGCONFIG);
    }
}
