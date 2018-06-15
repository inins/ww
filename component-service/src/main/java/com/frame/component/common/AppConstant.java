package com.frame.component.common;

import com.frame.component.api.Api;

public class AppConstant {

    //常量
    public static class Constant {
        //录音缓存目录
        public static final String SOUND_CACHE_DIR = "Sound";
        //Glide缓存目录
        public static final String GLIDE_CACHE_DIR = "Glide";
        //图片缓存目录
        public static final String IMAGE_CACHE_DIR = "Image";
        //背景图片缓存目录
        public static final String BACKGROUND_CACHE_DIR = "Background";
    }

    //key
    public static class Key {
        /********************************** 路由跳转Target ********************************/
        /**
         * 系统消息
         */
        public static final String OPEN_TARGET_SYS_MESSAGE = "sys_message";
        /**
         * 好友申请
         */
        public static final String OPEN_TARGET_FRIEND_APPLY = "friend_apply";
        /**
         * 群申请
         */
        public static final String OPEN_TARGET_GROUP_APPLY = "group_apply";
        /**
         * 群邀请
         */
        public static final String OPEN_TARGET_GROUP_INVITE = "group_invite";
        /**
         * 动态：趣晒
         */
        public static final String OPEN_TARGET_DYNAMIC_FUN_SHOW = "dynamic_fun_show";
        /**
         * 动态：话题
         */
        public static final String OPEN_TARGET_DYNAMIC_TOPIC = "dynamic_topic";
        /**
         * 会话：私聊
         */
        public static final String OPEN_TARGET_C2C = "cvs_c2c";
        /**
         * 会话：觅聊
         */
        public static final String OPEN_TARGET_TEAM = "cvs_team";
        /**
         * 会话：趣聊
         */
        public static final String OPEN_TARGET_SOCIAL = "cvs_social";
        /************************************************************************************/
    }

    //静态web页面url
    public static class Url {

        //##################################
        //############公共（文案）##########
        //##################################

        //快活林规则说明
        public static final String sharedTree = Api.WEB_BASE_URL + "about/common/sharedTree.html";
        //代言收益规则说明
        public static final String ruleDescription = Api.WEB_BASE_URL + "about/common/ruleDescription.html";
        //隐私协议
        public static final String privacyAgreement = Api.WEB_BASE_URL + "about/common/privacyAgreement.html";
        //建群须知
        public static final String notice = Api.WEB_BASE_URL + "about/common/notice.html";
        //邀请码
        public static final String InvitationCode = Api.WEB_BASE_URL + "about/common/InvitationCode.html";
        //趣聊付费须知
        public static final String fanNotice = Api.WEB_BASE_URL + "about/common/fanNotice.html";
        //觅聊付费须知
        public static final String MI_NOTICE = Api.WEB_BASE_URL + "about/common/MiNotice.html";
        //往往免责声明
        public static final String disclaimer = Api.WEB_BASE_URL + "about/common/disclaimer.html";
        //账户常见问题
        public static final String commonProblem = Api.WEB_BASE_URL + "about/android/commonProblem.html";
        //大佬撒钱规则介绍
        public static final String bigMoneyNotice = Api.WEB_BASE_URL + "about/common/bigMoneyNotice.html";
        //关于往往
        public static final String wwAbout = Api.WEB_BASE_URL + "about/common/wwAbout.html";
        //意见反馈：为什么希望得到你的意见
        public static final String proposal = Api.WEB_BASE_URL + "about/common/proposal.html";
        //分身说明
        public static final String REFERRAL_SHADOW = Api.WEB_BASE_URL + "about/common/Avatar.html";
        //觅聊说明（成员）
        public static final String REFERRAL_TEAM = Api.WEB_BASE_URL + "about/common/miliaoMember.html";
        //觅聊说明（创建觅聊）
        public static final String REFERRAL_TEAM_CREATE = Api.WEB_BASE_URL + "about/common/miliaoGroup.html";

        //##################################
        //#########android（文案）##########
        //##################################

        //关于账户
        public static final String aboutAccount = Api.WEB_BASE_URL + "about/common/Account.html";
        //关于趣聊
        public static final String aboutFanliao = Api.WEB_BASE_URL + "about/common/interest.html";
        //其它问题(下载与注册，关于我们)
        public static final String aboutOther = Api.WEB_BASE_URL + "about/common/Other.html";
        //关于趣晒/话题
        public static final String aboutQushai = Api.WEB_BASE_URL + "about/common/conversation.html";
        //往往用户注册协议
        public static final String userAgreement = Api.WEB_BASE_URL + "about/common/registerAgreement.html";
        //v2.0
        public static final String currentVersion = Api.WEB_BASE_URL + "about/common/v2.0.html";

        //##################################
        //############分享（文案）##########
        //##################################

        //游戏玩法
        public static final String cashcowrule = Api.WEB_BASE_URL + "contentShared/cashcow/rule.html";
        // 二维码
        public static final String ewmCode = Api.WEB_BASE_URL + "ewmCode/qrcode.html";
        //榜单
        public static final String overall = Api.WEB_BASE_URL + "overall/index.html";
        //下载
        public static final String appdownload = Api.WEB_BASE_URL + "app/index.html";

        //##################################
        //############  APP  ###############
        //##################################

        //给个好评，跳转页面
        public static final String eva = "http://t.cn/RBSarFb";
    }

    /**
     * 分享文案
     */
    public static class Share {
        public static final String SHARE_DEFAULT_IMAGE = "http://static.wangsocial.com/1024.png";

        /*群分享*/
        public static final String SHARE_GROUP_OPEN_TARGET = "group";
        public static final String SHARE_GROUP_URL = Api.WEB_BASE_URL + "contentShared/group/index.html?groupId=%1$s&userId=%2$s";
        public static final String SHARE_GROUP_TITLE = "整天哔哔哔不如当面来“怼”！";
        public static final String SHARE_GROUP_CONTENT = "%s在往往等你！";

        /*趣晒分享*/
        public static final String SHARE_FUN_SHOW_OPEN_TARGET = "story";
        public static final String SHARE_FUN_SHOW_URL = Api.WEB_BASE_URL + "contentShared/talk/index.html?talkId=%1$s&userId=%2$s";
        public static final String SHARE_FUN_SHOW_TITLE = "每个人都是这里的艺术家！";
        public static final String SHARE_FUN_SHOW_CONTENT = "%s有趣晒，你有酒吗？";

        /*话题分享*/
        public static final String SHARE_TOPIC_OPEN_TARGET = "topic";
        public static final String SHARE_TOPIC_URL = Api.WEB_BASE_URL + "contentShared/topic/index.html?topicId=%1$s&userId=%2$s";
        public static final String SHARE_TOPIC_TITLE = "每个人都是这里的艺术家！";
        public static final String SHARE_TOPIC_CONTENT = "%s有话题，你有酒吗？";

        /*APP分享*/
        public static final String SHARE_APP_URL = Api.WEB_BASE_URL + "app/index.html?userId=%s";
        public static final String SHARE_APP_TITLE = "左手社交右手射钱，触手可得！";
        public static final String SHARE_APP_CONTENT = "%s在往往等你！";
        public static final String SHARE_APP_IMAGE = SHARE_DEFAULT_IMAGE;

        /*摇钱树*/
        public static final String SHARE_GAME_TREE_OPEN_TARGET = "cashcow";
        public static final String SHARE_GAME_TREE_URL = Api.WEB_BASE_URL + "contentShared/cashcow/index.html?userId=%1$s&cashcowId=%2$s";
        public static final String SHARE_GAME_TREE_TITLE = "就差你了-这棵摇钱树将于20秒后掉落钻石！";
        public static final String SHARE_GAME_TREE_CONTENT = "%s想约你跟他一起分这波钻，快来！";
        public static final String SHARE_GAME_DEFAULT_IMAGE = "http://resouce.dongdongwedding.com/new_activity_moneyTree.png";

        /*代言收益分享*/
        public static final String SHARE_PROFIT_OPEN_TARGET = "profit";
        public static final String SHARE_PROFIT_URL = Api.WEB_BASE_URL + "contentShared/Profit/index.html?userId=%1$s";
        public static final String SHARE_PROFIT_TITLE = "有点2的社交，不一样的玩法";
        public static final String SHARE_PROFIT_CONTENT = "%s在往往等你！";
        public static final String SHARE_PROFIT_IMAGE = SHARE_DEFAULT_IMAGE;
    }
}