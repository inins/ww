package com.wang.social.im.mvp.ui.PersonalCard.model.entities.DTO;

import com.frame.component.ui.acticity.tags.Tag;
import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.EntitiesUtil;
import com.frame.component.entities.PersonalInfo;

import java.util.ArrayList;
import java.util.List;

public class SearchUserInfoDTO implements Mapper<PersonalInfo> {
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
    private Integer isFirend;

    @Override
    public PersonalInfo transform() {
        PersonalInfo object = new PersonalInfo();

        object.setBirthday(EntitiesUtil.assertNotNull(birthday));
        object.setProvince(EntitiesUtil.assertNotNull(province));
        object.setCity(EntitiesUtil.assertNotNull(city));
        object.setCityName(EntitiesUtil.assertNotNull(cityName));
        object.setPicList(picList == null ? new ArrayList<>() : picList);
        object.setSex(EntitiesUtil.assertNotNull(sex));
        object.setNickname(EntitiesUtil.assertNotNull(nickname));
        object.setAutograph(EntitiesUtil.assertNotNull(autograph));
        object.setAvatar(EntitiesUtil.assertNotNull(avatar));
        object.setProvinceName(EntitiesUtil.assertNotNull(provinceName));
        object.setUserId(EntitiesUtil.assertNotNull(userId));
        object.setTags(null == tags ? new ArrayList<>() : tags);
        object.setIsFriend(EntitiesUtil.assertNotNull(isFirend));

        return object;
    }
}
