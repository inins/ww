package com.frame.component.ui.acticity.BGMList;

import java.util.List;

import lombok.Data;

@Data
public class Musics {
    int current;
    int total;
    int pages;
    int size;
    List<Music> list;
}
