package com.frame.component.entities.dto;

import com.frame.component.entities.PersonalInfo;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PersonalInfoDTO implements Mapper<PersonalInfo> {
    private Long birthday;
    private String province;
    private String cityName;
    private String city;
    private List<String> picList;
    private Integer sex;
    private String nickname;
    private String autograph;
    private String avatar;
    private String provinceName;
    private Integer userId;
    private List<Tag> tags;
    // 是否是好友关系
    // 0：非好友，大于0：好友
    private Integer isFriend;
    // Integer  是否拉黑用户
    // 0：未拉黑，大于0：黑名单
    private Integer isBlack;


    @Override
    public PersonalInfo transform() {
        PersonalInfo object = new PersonalInfo();

        object.setBirthday(EntitiesUtil.assertNotNull(birthday));
        object.setProvince(EntitiesUtil.assertNotNull(province));
        object.setCityName(EntitiesUtil.assertNotNull(cityName));
        object.setCity(EntitiesUtil.assertNotNull(city));
        object.setPicList(null == picList ? new ArrayList<>() : picList);
        object.setSex(EntitiesUtil.assertNotNull(sex));
        object.setNickname(EntitiesUtil.assertNotNull(nickname));
        object.setAutograph(EntitiesUtil.assertNotNull(autograph));
        object.setAvatar(EntitiesUtil.assertNotNull(avatar));
        object.setProvinceName(EntitiesUtil.assertNotNull(provinceName));
        object.setUserId(EntitiesUtil.assertNotNull(userId));
        object.setTags(null == tags ? new ArrayList<>() : tags);
        object.setIsFriend(EntitiesUtil.assertNotNull(isFriend));
        object.setIsBlack(EntitiesUtil.assertNotNull(isBlack));

        return object;
    }
}
