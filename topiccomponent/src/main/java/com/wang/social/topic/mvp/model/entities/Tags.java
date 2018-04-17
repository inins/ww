package com.wang.social.topic.mvp.model.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Tags {
    List<Tag> list = new ArrayList<>();
}
