package com.wang.social.funpoint.mvp.entities;

import lombok.Data;

@Data
public class Lable {

    private String name;

    public Lable(String name) {
        this.name = name;
    }
}
