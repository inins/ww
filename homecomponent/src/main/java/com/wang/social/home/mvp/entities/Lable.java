package com.wang.social.home.mvp.entities;

import com.frame.component.entities.BaseSelectBean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Lable extends BaseSelectBean {

    private int index;

    private String name;

    public Lable(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public Lable(int index, String name, boolean isSelect) {
        this.index = index;
        this.name = name;
        this.isSelect = isSelect;
    }
}
