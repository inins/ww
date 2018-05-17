package com.wang.social.moneytree.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Keys {
    public final static String NAME_GROUP_ID = "NAME_GROUP_ID";
    public final static String NAME_GAME_TYPE = "NAME_GAME_TYPE";
    public final static String NAME_GAME_BEAN = "NAME_GAME_BEAN";
    public final static String NAME_GAME_RECORD = "NAME_GAME_RECORD";

    // 从聊天室进入
    public final static int TYPE_FROM_GROUP = 1;
    // 从首页的活动与游戏进入
    public final static int TYPE_FROM_SQUARE = 2;
    @IntDef({
            TYPE_FROM_GROUP,
            TYPE_FROM_SQUARE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface GameType {}
}
