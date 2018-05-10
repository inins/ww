package com.wang.social.home.mvp.entities.card;

import com.frame.component.entities.Tag;
import com.frame.utils.StrUtil;

import java.util.List;

import lombok.Data;

@Data
public class CardUser {


    /**
     * userId : 10008
     * nickname : 風流卍才子
     * avatar : https://thirdqq.qlogo.cn/qqapp/1106099147/C31DD5649207E2D160636DD6BC9F1CA8/100
     * sex : 0
     * birthday : 631123200000
     * constellation : 射手座
     * userTags : []
     * photoList : []
     * province : 四川省
     * city : 成都市
     */

    private int userId;
    private String nickname;
    private String avatar;
    private int sex;
    private long birthday;
    private String constellation;
    private String province;
    private String city;
    private List<Tag> userTags;
    private List<CardPic> photoList;

    private int index;

    public CardUser(int index) {
        this.index = index;
    }

    public boolean isMale() {
        return sex == 0;
    }

    public String getTagText() {
        if (StrUtil.isEmpty(userTags)) return "";
        String tagText = "# ";
        for (Tag tag : userTags) {
            tagText += tag.getTagName() + " ";
        }
        return tagText.trim();
    }

    public int getPicCount() {
        return photoList != null ? photoList.size() : 0;
    }
}
