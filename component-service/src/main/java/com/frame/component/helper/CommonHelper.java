package com.frame.component.helper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.frame.component.entities.User;
import com.frame.component.enums.ConversationType;
import com.frame.component.enums.ShareSource;
import com.frame.component.path.FunshowPath;
import com.frame.component.path.AppPath;
import com.frame.component.path.HomePath;
import com.frame.component.path.ImPath;
import com.frame.component.path.LoginPath;
import com.frame.component.path.MoneyTreePath;
import com.frame.component.path.PersonalPath;
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

        public static int getUserId() {
            User user = AppDataHelper.getUser();
            if (user != null) return user.getUserId();
            else return 0;
        }

        public static String getUserName() {
            User user = AppDataHelper.getUser();
            if (user != null) return user.getNickname();
            else return "";
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

    public static class AppHelper {
        public static void startHomeActivity(Context context) {
            UIRouter.getInstance().openUri(context, AppPath.HOME_URL, null);
        }

        public static void startSearchActivity(Context context) {
            UIRouter.getInstance().openUri(context, AppPath.SEARCH_URL, null);
        }
    }

    public static class HomeHelper {

        public static void startUserDetailActivity(Context context, int userId) {
            Bundle bundle = new Bundle();
            bundle.putInt("userId", userId);
            UIRouter.getInstance().openUri(context, HomePath.USERDETAIL_URL, bundle);
        }
    }

    public static class PersonalHelper {

        //启动代言收益页面
        public static void startProfitActivity(Context context) {
            UIRouter.getInstance().openUri(context, PersonalPath.PROFIT_URL, null);
        }

        //启动个人账户页面
        public static void startAccountActivity(Context context) {
            UIRouter.getInstance().openUri(context, PersonalPath.ACCOUNT_URL, null);
        }

        //启动充值页面
        public static void startRechargeActivity(Context context) {
            UIRouter.getInstance().openUri(context, PersonalPath.RECHARGE_URL, null);
        }

        //启动官方图库页面
        public static void startOfficialPhotoActivity(Context context) {
            UIRouter.getInstance().openUri(context, PersonalPath.OFFICIAL_URL, null);
        }

        //启动官方图库页面
        public static void startOfficialPhotoActivity(Activity activity, int requestCode) {
            UIRouter.getInstance().openUri(activity, PersonalPath.OFFICIAL_URL, null, requestCode);
        }
    }

    public static class FunshowHelper {
        //趣晒魔页面
        public static void startHotUserActivity(Context context) {
            UIRouter.getInstance().openUri(context, FunshowPath.FUNSHOW_HOTUSER_URL, null);
        }

        //发布趣晒页面
        public static void startAddActivity(Context context) {
            UIRouter.getInstance().openUri(context, FunshowPath.FUNSHOW_ADD_URL, null);
        }

        //趣晒详情页面
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
        @Deprecated
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
        @Deprecated
        public static void gotoTeamConversation(Context context, String targetId) {
            Bundle bundle = new Bundle();
            bundle.putString("targetId", targetId);
            UIRouter.getInstance().openUri(context, ImPath.TEAM_URL, bundle);
        }

        /**
         * 趣聊/觅聊会话界面
         *
         * @param context
         * @param targetId
         * @param conversationType
         */
        public static void gotoGroupConversation(Context context, String targetId, ConversationType conversationType, boolean fromMirror) {
            Bundle bundle = new Bundle();
            bundle.putString("targetId", targetId);
            bundle.putInt("typeOrdinal", conversationType.ordinal());
            bundle.putBoolean("fromMirror", fromMirror);
            UIRouter.getInstance().openUri(context, ImPath.GROUP_URL, bundle);
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

        /**
         * 个人名片
         *
         * @param context context
         * @param type    类型 （1.好友申请，底部会显示 拒绝 同意 按钮 2.浏览模式，底部显示 添加好友）
         * @param userId  用户id
         * @param msgId   消息id（如果是从通知消息中
         */
        private static void startPersonalCard(Context context, int type, int userId, int msgId) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", type);
            bundle.putInt("userid", userId);
            bundle.putInt("msgId", msgId);
            UIRouter.getInstance().openUri(context, ImPath.PERSONAL_CARD_URL, bundle);
        }

        /**
         * 从好友申请通知消息中进入个人名片
         *
         * @param context context
         * @param userId  用户id
         * @param msgId   消息id
         */
        public static void startPersonalCardForApplyFromMsg(Context context, int userId, int msgId) {
            startPersonalCard(context, 1, userId, msgId);
        }

        /**
         * 浏览个人名片
         *
         * @param context context
         * @param userId  用户id
         */
        public static void startPersonalCardForBrowse(Context context, int userId) {
            startPersonalCard(context, 2, userId, -1);
        }

        /**
         * 趣聊邀请和详情简要查看
         *
         * @param context       context
         * @param type          类型（0.默认浏览模式（底部显示 立即加入） 1.趣聊邀请，底部显示（拒绝 同意））
         * @param groupId       群id
         * @param msgId         消息id
         * @param isGroupMember 是否是群成员
         */
        private static void startGroupInvite(Context context, int type, int groupId,
                                             int msgId, int isGroupMember, boolean isRefused) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", type);
            bundle.putInt("groupid", groupId);
            bundle.putInt("msgid", msgId);
            bundle.putInt("isGroupMember", isGroupMember);
            bundle.putBoolean("isRefused", isRefused);
            UIRouter.getInstance().openUri(context, ImPath.GROUP_INVITE_URL, bundle);
        }

        /**
         * 从趣聊邀请消息中启动趣聊名片
         *
         * @param context context
         * @param groupId 群id
         * @param msgId   消息id
         */
        public static void startGroupInviteFromMsg(Context context, int groupId, int msgId, boolean isRefused) {
            startGroupInvite(context, 1, groupId, msgId, -1, isRefused);
        }

        /**
         * 浏览趣聊详情，底部显示立即加入
         *
         * @param context context
         * @param groupId 群id
         */
        public static void startGroupInviteBrowse(Context context, int groupId) {
            startGroupInvite(context, 0, groupId, -1, -1, false);
        }

        /**
         * 浏览趣聊详情，参数中带有是用户是否是该群成员
         *
         * @param context       context
         * @param groupId       群id
         * @param isGroupMember 是否是该群成员
         */
        public static void startGroupInviteBrowse(Context context, int groupId, int isGroupMember) {
            startGroupInvite(context, 0, groupId, -1, isGroupMember, false);
        }

        /**
         * 趣聊主页
         *
         * @param context  context
         * @param socialId 趣聊群id
         */
        public static void gotoSocialHome(Context context, String socialId) {
            Bundle bundle = new Bundle();
            bundle.putString("socialId", socialId);
            UIRouter.getInstance().openUri(context, ImPath.SOCIAL_HOME_URL, bundle);
        }

        /**
         * 启动往往官方号页面
         *
         * @param context context
         */
        public static void startOfficialChatRobot(Context context) {
            UIRouter.getInstance().openUri(context, ImPath.OFFICIAL_CHAT_ROBOT_URL, null);
        }

        /**
         * 往往内部分享
         *
         * @param context
         * @param title    分享标题
         * @param content  分享内容(若为分享游戏此字段表示游戏钻石数)
         * @param imageUrl 图片URL
         * @param source   来源
         * @param objectId 分享对象ID
         */
        public static void startWangWangShare(Context context, String title, String content, String imageUrl, ShareSource source, String objectId) {
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("content", content);
            bundle.putString("imageUrl", imageUrl);
            bundle.putInt("source", source == null ? -1 : source.ordinal());
            bundle.putString("objectId", objectId);
            UIRouter.getInstance().openUri(context, ImPath.SHARE_RECENTLY_URL, bundle);
        }

        /**
         * 从消息列表启动觅聊名片
         *
         * @param context context
         * @param groupId 觅聊id
         * @param msgId   消息id
         */
        public static void startTeamCardFromMsg(Context context, int groupId, int msgId) {
            Bundle bundle = new Bundle();
            bundle.putInt("groupid", groupId);
            bundle.putInt("msgid", msgId);
            UIRouter.getInstance().openUri(context, ImPath.TEAM_CARD_URL, bundle);
        }

        /**
         * 系统消息
         */
        public static void startNotifySysMsgActivity(Context context) {
            UIRouter.getInstance().openUri(context, ImPath.NOTIFY_SYS_URL, null);
        }

        /**
         * 好友申请
         */
        public static void startNotifyFriendRequestActivity(Context context) {
            UIRouter.getInstance().openUri(context, ImPath.NOTIFY_FRIEND_URL, null);
        }

        /**
         * 加入觅聊申请页面
         */
        public static void startNotifyGroupRequestActivity(Context context) {
            UIRouter.getInstance().openUri(context, ImPath.NOTIFY_GROUP_URL, null);
        }

        /**
         * 趣聊邀请
         */
        public static void startNotifyGroupJoinActivity(Context context) {
            UIRouter.getInstance().openUri(context, ImPath.NOTIFY_GROUP_JOIN_URL, null);
        }

        /**
         * 点赞列表
         *
         * @param context
         */
        public static void startNotifyPraise(Context context) {
            UIRouter.getInstance().openUri(context, ImPath.NOTIFY_PRAISE_LIST_URL, null);
        }

        /**
         * 评论列表
         *
         * @param context
         */
        public static void startNotifyComment(Context context) {
            UIRouter.getInstance().openUri(context, ImPath.NOTIFY_COMMENT_LIST_URL, null);
        }

        /**
         * @param context
         * @列表
         */
        public static void startNotifyAlert(Context context) {
            UIRouter.getInstance().openUri(context, ImPath.NOTIFY_ALERT_LIST_URL, null);
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
            Bundle bundle = new Bundle();
            bundle.putInt("NAME_TOPIC_ID", topicId);
            UIRouter.getInstance().openUri(context, TopicPath.TOPIC_DETAIL_URL, bundle);
        }

        /**
         * 知识魔列表
         */
        public static void startTopUser(Context context) {
            UIRouter.getInstance().openUri(context, TopicPath.TOPIC_TOP_USER_URL, null);
        }
    }

    public static class GameHelper {
        /**
         * 摇钱树游戏列表
         *
         * @param context context
         * @param type    创建类型 （1.从聊天室进入 2.从首页的活动与游戏进入）
         * @param groupId 群id，类型为1时必须设置这个参数
         */
        private static void startMoneyTree(Context context, int type, int groupId) {
            Bundle bundle = new Bundle();
            bundle.putInt("NAME_GAME_TYPE", type);
            bundle.putInt("NAME_GROUP_ID", groupId);
            UIRouter.getInstance().openUri(context, MoneyTreePath.MONEY_TREE_LIST_URL, bundle);
        }

        /**
         * 从群进入游戏列表
         *
         * @param context context
         * @param groupId 群id
         */
        public static void startMoneyTreeFromGroup(Context context, int groupId) {
            startMoneyTree(context, 1, groupId);
        }

        /**
         * 趣聊群进入游戏房间
         *
         * @param context context
         * @param roomId  房间id
         */
        public static void startGameRoom(Context context, int roomId) {
            Bundle bundle = new Bundle();
            bundle.putInt("NAME_GAME_TYPE", 1);
            bundle.putInt("NAME_ROOM_ID", roomId);
            UIRouter.getInstance().openUri(context, MoneyTreePath.MONEY_TREE_ROOM_URL, bundle);
        }

        /**
         * 从首页活动与游戏进入游戏列表
         *
         * @param context context
         */
        public static void startMoneyTreeFromSquare(Context context) {
            startMoneyTree(context, 2, -1);
        }
    }
}
