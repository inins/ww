package com.wang.social.topic.mvp.model.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TopicRsp {
    private int total;
    private int size;
    private int pages;
    private int current;

    private List<Topic> list = new ArrayList<>();

}
