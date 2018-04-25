package com.wang.social.topic.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.topic.mvp.model.entities.Music;
import com.wang.social.topic.mvp.model.entities.Musics;

import java.util.ArrayList;
import java.util.List;

public class MusicsDTO implements Mapper<Musics> {
    List<Music> list;

    @Override
    public Musics transform() {
        Musics object = new Musics();

        object.setList(list == null ? new ArrayList<>() : list);

        return object;
    }
}
