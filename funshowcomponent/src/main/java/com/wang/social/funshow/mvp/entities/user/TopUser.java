package com.wang.social.funshow.mvp.entities.user;

import com.frame.component.entities.Tag;
import com.frame.component.view.barview.BarUser;
import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TopUser {

    /**
     * userId : 10000
     * publishNum : 0
     * participateNum : 4
     * nickname : 飘飘飘香
     * constellation : 射手座
     * sex : 1
     * age : 25
     * birthday : 725904000000
     * tags : [{"tagId":5,"tagName":"综艺"},{"tagId":3,"tagName":"电影"},{"tagId":4,"tagName":"电视剧"},{"tagId":6,"tagName":"直播"}]
     */

    private int userId;
    private String avatar;
    private int publishNum;
    private int participateNum;
    private String nickname;
    private String constellation;
    private int sex;
    private int age;
    private long birthday;
    private List<Tag> tags;

    ////////////////////////////////////////

    //是不是男性
    public boolean isMale() {
        return sex == 0;
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

    public String getTagText() {
        if (StrUtil.isEmpty(tags)) return "";
        String tagText = "";
        for (Tag tag : tags) {
            tagText += "#" + tag.getTagName() + " ";
        }
        return tagText.trim();
    }

    public static List<BarUser> topUsers2BarUsers(List<TopUser> topUsers) {
        List<BarUser> barUsers = new ArrayList<>();
        if (!StrUtil.isEmpty(topUsers)) {
            for (TopUser topUser : topUsers) {
                barUsers.add(new BarUser(topUser.getAvatar()));
            }
        }
        return barUsers;
    }
}
