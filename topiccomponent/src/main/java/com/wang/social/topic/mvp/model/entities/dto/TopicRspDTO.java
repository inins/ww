package com.wang.social.topic.mvp.model.entities.dto;


import com.frame.http.api.Mapper;
import com.wang.social.topic.mvp.model.entities.Tag;
import com.wang.social.topic.mvp.model.entities.Tags;
import com.wang.social.topic.mvp.model.entities.Topic;
import com.wang.social.topic.mvp.model.entities.TopicRsp;

import java.util.ArrayList;
import java.util.List;


public class TopicRspDTO implements Mapper<TopicRsp> {
    private int total;
    private int size;
    private int pages;
    private int current;

    private List<Topic> list = new ArrayList<>();

    @Override
    public TopicRsp transform() {
        TopicRsp rsp = new TopicRsp();

        rsp.setTotal(total);
        rsp.setSize(size);
        rsp.setPages(pages);
        rsp.setCurrent(current);
        rsp.setList(list);

        return rsp;
    }
}