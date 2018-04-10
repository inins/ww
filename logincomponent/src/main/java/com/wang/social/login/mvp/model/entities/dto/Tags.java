package com.wang.social.login.mvp.model.entities.dto;

import com.wang.social.login.mvp.model.entities.Tag;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Tags {
    List<Tag> list = new ArrayList<>();

    public List<Tag> getList() {
        return list;
    }

    public void setList(List<Tag> list) {
        this.list = list;
    }
}
