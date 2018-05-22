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
}