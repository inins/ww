package com.wang.social.personal.mvp.entities.lable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by Administrator on 2018/3/29.
 */
@Data
public class Lable implements Serializable {

    /**
     * "id": 2,
     * "tagName": "标签名称",
     * "isMi": 1
     */

    private int id;
    @SerializedName("tagName")
    private String name;
    @SerializedName("isMi")
    private int showTag;

    public Lable() {
    }

    public Lable(String name) {
        this.name = name;
    }

    public Lable(String name, int showTag) {
        this.name = name;
        this.showTag = showTag;
    }

    //////////////////////////////////

    public boolean getShowTagBool() {
        return showTag == 1;
    }
    //////////////////////////////////

}
