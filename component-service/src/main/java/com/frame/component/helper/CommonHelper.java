package com.frame.component.helper;

import android.content.Context;
import android.os.Bundle;

import com.frame.component.path.FunshowPath;
import com.frame.component.path.AppPath;
import com.frame.component.path.ImPath;
import com.frame.component.path.LoginPath;
import com.frame.component.path.TopicPath;
import com.frame.component.router.ui.UIRouter;
import com.frame.utils.ToastUtil;

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
            UIRouter.getInstance().openUri(context, AppPath.HOME_URL, null);
        }

        public static void startSearchActivity(Context context) {
            UIRouter.getInstance().openUri(context, AppPath.SEARCH_URL, null);
        }
    }

    public static class FunshowHelper {
        public static void startAddActivity(Context context) {
            UIRouter.getInstance().openUri(context, FunshowPath.FUNSHOW_ADD_URL, null);
        }

        public static void startDetailActivity(Context context, int talkId) {
            Bundle bundle = new Bundle();
            bundle.putInt("talkId", talkId);
            UIRouter.getInstance().openUri(context, FunshowPath.FUNSHOW_DETAIL_URL, bundle);
        }
    }

    public static class ImHelper {

        /**
         * 个人聊天界面
         *
         * @param context
         * @param targetId
         */
        public static void gotoPrivateConversation(Context context, String targetId) {
            Bundle bundle = new Bundle();
            bundle.putString("targetId", targetId);
            UIRouter.getInstance().openUri(context, ImPath.PRIVATE_URL, bundle);
        }

        /**
         * 趣聊会话界面
         *
         * @param context
         * @param targetId
         */
        public static void gotoSocialConversation(Context context, String targetId) {
            Bundle bundle = new Bundle();
            bundle.putString("targetId", targetId);
            UIRouter.getInstance().openUri(context, ImPath.SOCIAL_URL, bundle);
        }

        /**
         * 觅聊会话界面
         *
         * @param context
         * @param targetId
         */
        public static void gotoTeamConversation(Context context, String targetId) {
            Bundle bundle = new Bundle();
            bundle.putString("targetId", targetId);
            UIRouter.getInstance().openUri(context, ImPath.TEAM_URL, bundle);
        }

        /**
         * 分享树
         *
         * @param context
         * @param type
         * @param objectId
         */
        public static void gotoShareWood(Context context, int type, String objectId) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", type);
            bundle.putString("objectId", objectId);
            UIRouter.getInstance().openUri(context, ImPath.SHARE_WOOD_URL, bundle);
        }
    }

    public static class TopicHelper {
        /**
         * 话题发布
         *
         * @param context context
         */
        public static void startTopicRelease(Context context) {
            UIRouter.getInstance().openUri(context, TopicPath.TOPIC_RELEASE_URL, null);
        }

        /**
         * 详情话题
         */
        public static void startTopicDetail(Context context, int topicId) {
            //TODO:暂时没有，预留一个方法
            ToastUtil.showToastShort("topicId:" + topicId);
        }
    }
}
