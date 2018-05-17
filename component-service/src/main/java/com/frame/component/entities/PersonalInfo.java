package com.frame.component.entities;

import com.frame.component.ui.acticity.tags.Tag;

import java.util.List;

import lombok.Data;

@Data
public class PersonalInfo {
    private long birthday;
    private String province;
    private String cityName;
    private String city;
    private List<String> picList;
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
}