package com.frame.component.ui.acticity.BGMList;

import com.frame.http.api.Mapper;

import java.util.ArrayList;
import java.util.List;

public class MusicsDTO implements Mapper<Musics> {
    int current;
    int total;
    int pages;
    int size;
    List<Music> list;

    @Override
    public Musics transform() {
        Musics object = new Musics();

        object.setCurrent(current);
        object.setTotal(total);
        object.setPages(pages);
        object.setSize(size);
        object.setList(list == null ? new ArrayList<>() : list);

        return object;
    }
}
