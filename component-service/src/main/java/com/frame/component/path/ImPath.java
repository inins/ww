package com.frame.component.path;

/**
 * ============================================
 * 聊天相关Path
 * <p>
 * Create by ChenJing on 2018-04-23 10:58
 * ============================================
 */
public interface ImPath extends BasePath {

    String HOST = "im";

    //个人聊天页面
    String PRIVATE_PATH = "/private";
    String PRIVATE_URL = SCHEME + HOST + PRIVATE_PATH;

    //趣聊/觅聊页面
    String GROUP_PATH = "/group";
    String GROUP_URL = SCHEME + HOST + GROUP_PATH;

    //趣聊页面
    String SOCIAL_PATH = "/social";
    String SOCIAL_URL = SCHEME + HOST + SOCIAL_PATH;

    //趣聊页面
    String TEAM_PATH = "/team";
    String TEAM_URL = SCHEME + HOST + TEAM_PATH;

    //分享树
    String SHARE_WOOD_PATH = "/shareWood";
    String SHARE_WOOD_URL = SCHEME + HOST + SHARE_WOOD_PATH;

    // 个人名片
    String PERSONAL_CARD_PATH = "/personal_card";
    String PERSONAL_CARD_URL = SCHEME + HOST + PERSONAL_CARD_PATH;

    // 趣聊邀请
    String GROUP_INVITE_PATH = "/group_invite";
    String GROUP_INVITE_URL = SCHEME + HOST + GROUP_INVITE_PATH;

    //趣聊详情页
    String SOCIAL_HOME = "/social_home";
    String SOCIAL_HOME_URL = SCHEME + HOST + SOCIAL_HOME;

    // 往往官方号
    String OFFICIAL_CHAT_ROBOT = "/official_chat_robot";
    String OFFICIAL_CHAT_ROBOT_URL = SCHEME + HOST + OFFICIAL_CHAT_ROBOT;

    //分享到往往
    String SHARE_RECENTLY = "/share_recently";
    String SHARE_RECENTLY_URL = SCHEME + HOST + SHARE_RECENTLY;

    // 觅聊名片
    String TEAM_CARD = "/team_invite_card";
    String TEAM_CARD_URL = SCHEME + HOST + TEAM_CARD;

    // 系统消息
    String NOTIFY_SYS = "/notify_sys";
    String NOTIFY_SYS_URL = SCHEME + HOST + NOTIFY_SYS;

    // 好友申请
    String NOTIFY_FRIEND = "/notify_friend_request";
    String NOTIFY_FRIEND_URL = SCHEME + HOST + NOTIFY_FRIEND;

    // 加入觅聊申请页面
    String NOTIFY_GROUP = "/notify_group_request";
    String NOTIFY_GROUP_URL = SCHEME + HOST + NOTIFY_GROUP;

    // 趣聊邀请
    String NOTIFY_GROUP_JOIN = "/notify_group_join";
    String NOTIFY_GROUP_JOIN_URL = SCHEME + HOST + NOTIFY_GROUP_JOIN;
}