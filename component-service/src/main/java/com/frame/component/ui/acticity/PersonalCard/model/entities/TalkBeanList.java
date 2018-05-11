package com.frame.component.ui.acticity.PersonalCard.model.entities;

import com.frame.component.ui.acticity.tags.Tag;

import java.util.List;

import lombok.Data;

@Data
public class TalkBeanList {
    private int current;
    private int total;
    private int pages;
    private int size;
    private List<TalkBean> list;
}
