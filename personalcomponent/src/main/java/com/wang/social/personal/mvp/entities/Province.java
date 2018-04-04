package com.wang.social.personal.mvp.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/2.
 */

public class Province implements Serializable{
    private int id;
    private String name;

    public Province() {
    }

    public Province(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
