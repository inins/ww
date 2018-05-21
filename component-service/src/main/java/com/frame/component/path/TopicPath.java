package com.frame.component.path;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-23 17:27
 * =========================================
 */

public interface TopicPath extends BasePath{

    String HOST = "topic";

    // 话题发布
    String TOPIC_RELEASE = "/topic_release";
    String TOPIC_RELEASE_URL = SCHEME + HOST + TOPIC_RELEASE;

    // 话题详情
    String TOPIC_DETAIL = "/topic_detail";
    String TOPIC_DETAIL_URL = SCHEME + HOST + TOPIC_DETAIL;

    // 知识魔列表
    String TOPIC_TOP_USER = "/top_user_list";
    String TOPIC_TOP_USER_URL = SCHEME + HOST + TOPIC_TOP_USER;
}