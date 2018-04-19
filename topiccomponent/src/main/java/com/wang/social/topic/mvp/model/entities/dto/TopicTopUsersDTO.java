package com.wang.social.topic.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.topic.mvp.model.entities.TopicTopUser;
import com.wang.social.topic.mvp.model.entities.TopicTopUsers;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TopicTopUsersDTO implements Mapper<TopicTopUsers> {
    List<TopicTopUser> list;

    @Override
    public TopicTopUsers transform() {
        TopicTopUsers object = new TopicTopUsers();

        object.setList(list == null ? new ArrayList<>() : list);

        return object;
    }
}
