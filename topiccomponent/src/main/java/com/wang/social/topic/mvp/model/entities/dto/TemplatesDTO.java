package com.wang.social.topic.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.topic.mvp.model.entities.Template;
import com.wang.social.topic.mvp.model.entities.Templates;

import java.util.ArrayList;
import java.util.List;

public class TemplatesDTO implements Mapper<Templates> {
    List<Template> list;

    @Override
    public Templates transform() {
        Templates object = new Templates();

        object.setList(null == list ? new ArrayList<>() : list);

        return object;
    }
}
