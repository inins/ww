package com.frame.component.ui.acticity.tags;

import com.frame.http.api.Mapper;

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