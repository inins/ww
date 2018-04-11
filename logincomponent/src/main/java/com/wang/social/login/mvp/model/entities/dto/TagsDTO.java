package com.wang.social.login.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.login.mvp.model.entities.Tag;

import java.util.ArrayList;
import java.util.List;


public class TagsDTO implements Mapper<Tags>{
    private List<Tag> list = new ArrayList<>();

    @Override
    public Tags transform() {
        Tags tags = new Tags();
        tags.setList(list);
        return tags;
    }
}