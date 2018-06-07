package com.wang.social.home.mvp.entities.card;

import com.frame.component.entities.Tag;
import com.frame.component.entities.photo.Photo;
import com.frame.component.entities.user.UserBoard;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class CardUser implements Serializable {


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
    private int showAge;
    private long birthday;
    private String constellation;
    private String province;
    private String city;
    private List<Tag> userTags;
    private List<Photo> photoList;

    private int index;

    public CardUser(int index) {
        this.index = index;
    }

    public boolean isMale() {
        return sex == 0;
    }

    public boolean isShowAge() {
        return showAge != 0;
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

    public UserBoard tans2UserBoard() {
        UserBoard userBoard = new UserBoard();
        userBoard.setUserId(userId);
        userBoard.setNickname(nickname);
        userBoard.setAvatar(avatar);
        userBoard.setProvince(province);
        userBoard.setCityName(city);
        userBoard.setPicList(photoList);
        userBoard.setTags(userTags);
        userBoard.setSex(sex);
        userBoard.setBirthday(birthday);

        return userBoard;
    }
}
