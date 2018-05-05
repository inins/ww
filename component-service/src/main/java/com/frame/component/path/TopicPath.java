package com.frame.component.path;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-23 17:27
 * =========================================
 */

public interface TopicPath extends BasePath{

    String HOST = "topic/";

    // 话题发布
    String TOPIC_RELEASE = "topic_release";
    String TOPIC_RELEASE_URL = SCHEME + HOST + TOPIC_RELEASE;
}