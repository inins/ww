package com.wang.social.home.mvp.entities;

import com.frame.component.entities.BaseSelectBean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Lable extends BaseSelectBean {

    private String name;

    public Lable(String name) {
        this.name = name;
    }

    public Lable(String name, boolean isSelect) {
        this.name = name;
        this.isSelect = isSelect;
    }
}
