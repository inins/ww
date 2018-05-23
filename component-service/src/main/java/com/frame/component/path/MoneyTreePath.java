package com.frame.component.path;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-23 17:27
 * =========================================
 */

public interface MoneyTreePath extends BasePath{

    String HOST = "moneytree";

    // 游戏列表
    String MONEY_TREE_LIST = "/money_tree_list";
    String MONEY_TREE_LIST_URL = SCHEME + HOST + MONEY_TREE_LIST;

    // 游戏房间
    String MONEY_TREE_ROOM = "/money_tree_room";
    String MONEY_TREE_ROOM_URL = SCHEME + HOST + MONEY_TREE_ROOM;
}