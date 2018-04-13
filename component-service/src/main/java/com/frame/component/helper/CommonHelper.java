package com.frame.component.helper;

import android.content.Context;

import com.frame.component.path.HomePath;
import com.frame.component.path.LoginPath;
import com.frame.component.router.ui.UIRouter;

//公用的帮助类
public class CommonHelper {

    //登录帮助类
    public static class LoginHelper {

        public static boolean isLogin() {
            return AppDataHelper.getUser() != null;
        }

        public static void startLoginActivity(Context context) {
            UIRouter.getInstance().openUri(context, LoginPath.LOGIN_URL, null);
        }

        public static void startTagSelectActivity(Context context) {
            UIRouter.getInstance().openUri(context, LoginPath.LOGIN_TAG_SELECTION_URL, null);
        }

        public static void startBindPhoneActivity(Context context) {
            UIRouter.getInstance().openUri(context, LoginPath.LOGIN_BIND_PHONE_URL, null);
        }

        public static void startSetPswActivity(Context context) {
            //跳转验证手机页面，该页面验证成功后会跳转设置密码页面
            UIRouter.getInstance().openUri(context, LoginPath.LOGIN_VERIFY_PHONE_URL, null);
        }
    }

    public static class HomeHelper {
        public static void startHomeActivity(Context context) {
            UIRouter.getInstance().openUri(context, HomePath.HOME_URL, null);
        }
    }
}
