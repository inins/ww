package com.wang.social.home.helper;

import com.frame.component.entities.User;
import com.frame.utils.SPUtils;
import com.wang.social.home.mvp.entities.FunpointAndTopic;
import com.wang.social.home.mvp.entities.card.CardGroup;
import com.wang.social.home.mvp.entities.card.CardUser;
import com.wang.social.home.mvp.entities.funshow.FunshowHome;

import java.util.List;

public class CatchHelper {
    private static final String SHARENAME = "app_catch";
    private static final String KEY_CARD_USER = "card_user";
    private static final String KEY_CARD_GROUP = "card_group";
    private static final String KEY_HOME_FUNSHOW = "home_funshow";
    private static final String KEY_HOME_CONTENT = "home_contrent";

    ////////////////// 持久化同类的人数据 /////////////////

    public static void saveCardUser(List<CardUser> cardUsers) {
        if (cardUsers == null) return;
        SPUtils.getInstance(SHARENAME).put(KEY_CARD_USER, cardUsers);
    }

    public static List<CardUser> getCardUser() {
        return (List<CardUser>) SPUtils.getInstance(SHARENAME).get(KEY_CARD_USER);
    }

    ////////////////// 持久化同类的圈子数据 /////////////////

    public static void saveCardGroup(List<CardGroup> cardGroups) {
        if (cardGroups == null) return;
        SPUtils.getInstance(SHARENAME).put(KEY_CARD_GROUP, cardGroups);
    }

    public static List<CardGroup> getCardGroup() {
        return (List<CardGroup>) SPUtils.getInstance(SHARENAME).get(KEY_CARD_GROUP);
    }

    ////////////////// 持久化首页趣晒数据 /////////////////

    public static void saveFunshowHome(FunshowHome funshowHome) {
        if (funshowHome == null) return;
        SPUtils.getInstance(SHARENAME).put(KEY_HOME_FUNSHOW, funshowHome);
    }

    public static FunshowHome getFunshowHome() {
        return (FunshowHome) SPUtils.getInstance(SHARENAME).get(KEY_HOME_FUNSHOW);
    }

    ////////////////// 持久化首页内容 /////////////////

    public static void saveHomeContent(List<FunpointAndTopic> funpointAndTopics) {
        if (funpointAndTopics == null) return;
        SPUtils.getInstance(SHARENAME).put(KEY_HOME_CONTENT, funpointAndTopics);
    }

    public static List<FunpointAndTopic> getHomeContent() {
        return (List<FunpointAndTopic>) SPUtils.getInstance(SHARENAME).get(KEY_HOME_CONTENT);
    }
}
