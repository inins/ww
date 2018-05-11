package com.frame.component.ui.acticity.PersonalCard.model.entities;

import com.frame.component.ui.acticity.tags.Tag;
import java.util.List;

import lombok.Data;

@Data
public class UserInfo {
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
}
