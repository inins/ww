package com.frame.component.entities;

import com.frame.component.api.Api;
import com.frame.utils.StrUtil;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by liaoinstan on 2018/4/3.
 * 用户实体
 */
@Data
public class ShatDownUser implements Serializable {

    /**
     * id : 1
     * userId : 10000
     * nickname : 平东
     * avatar : http://resouce.dongdongwedding.com/Fuqg4sWTwi-D-7oqmqIkPa3ARBzu
     * sex : 1
     * createTime : 1505232000000
     * phone : 15528801837
     * birthday : 1990-12-31
     * constellation : 射手座
     */

    private int id;
    private int userId;
    private String nickname;
    private String avatar;
    private int sex;
    private long birthday;
    private String autograph;
    private List<Tag> tags;

    /////////////////////////////////

    //是不是男性
    public boolean isMale() {
        return sex == 0;
    }

    public String getTagText() {
        if (StrUtil.isEmpty(tags)) return "";
        String tagText = "";
        for (Tag tag : tags) {
            tagText += "#" + tag.getTagName() + " ";
        }
        return tagText.trim();
    }

    public String getSexText() {
        if (sex == 0) {
            return "男";
        } else if (sex == 1) {
            return "女";
        } else {
            return "";
        }
    }
}
