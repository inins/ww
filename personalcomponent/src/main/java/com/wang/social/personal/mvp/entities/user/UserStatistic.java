package com.wang.social.personal.mvp.entities.user;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserStatistic implements Serializable{

    /**
     * friendCount : 1
     * groupCount : 1
     * talkCount : 1
     * topicCount : 1
     */

    private int friendCount;
    private int groupCount;
    private int talkCount;
    private int topicCount;
}
