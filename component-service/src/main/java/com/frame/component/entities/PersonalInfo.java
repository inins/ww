package com.frame.component.entities;

import com.frame.component.ui.acticity.tags.Tag;
import com.frame.utils.StrUtil;

import java.util.List;

import lombok.Data;

@Data
public class PersonalInfo {
    private long birthday;
    private String province;
    private String cityName;
    private String city;
    private List<PersonalPic> picList;
    private int sex;
    private String nickname;
    private String autograph;
    private String avatar;
    private String provinceName;
    private int userId;
    private List<Tag> tags;
    // 是否是好友关系
    // 0：非好友，大于0：好友
    private int isFriend;
    // Integer  是否拉黑用户
    // 0：未拉黑，大于0：黑名单
    private int isBlack;
    private String remarkName;
    private String remarkHeadImg;

    public boolean isMale() {
        return sex == 0;
    }

    public String getTagText(){
        if (StrUtil.isEmpty(tags)) return "";
        String tagText = "";
        for (Tag tag : tags) {
            tagText += "#" + tag.getTagName() + " ";
        }
        return tagText.trim();
    }
}
