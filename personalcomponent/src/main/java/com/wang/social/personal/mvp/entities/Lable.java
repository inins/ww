package com.wang.social.personal.mvp.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/29.
 */

public class Lable implements Serializable{

    /**
     * "id": 2,
     "tagName": "标签名称",
     "isMi": 1
     */

    private int id;
    @SerializedName("tagName")
    private String name;
    @SerializedName("isMi")
    private boolean showTag;

    public Lable() {
    }

    public Lable(String name) {
        this.name = name;
    }

    public Lable(String name, boolean showTag) {
        this.name = name;
        this.showTag = showTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowTag() {
        return showTag;
    }

    public void setShowTag(boolean showTag) {
        this.showTag = showTag;
    }
}
