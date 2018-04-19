package com.wang.social.topic.mvp.model.entities;

import java.util.List;
import lombok.Data;

@Data
public class TopicTopUser {
    private int birthday;
    private String avatar;
    private String nickname;
    private int topicPartakeCount;
    private int sex;
    private List<String> tags ;
}
