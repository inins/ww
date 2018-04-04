package com.wang.social.personal.mvp.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/2.
 */

public class City implements Serializable {
    private int id;
    private int provinceId;
    private String name;

    public City() {
    }

    public City(int id, int provinceId, String name) {
        this.id = id;
        this.provinceId = provinceId;
        this.name = name;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
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
