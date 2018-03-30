package com.wang.social.personal.mvp.entities;

import com.frame.component.entities.BaseSelectBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/30.
 */

public class ShowListGroup extends BaseSelectBean implements Serializable{

    private String title;

    public ShowListGroup() {
    }

    public ShowListGroup(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
