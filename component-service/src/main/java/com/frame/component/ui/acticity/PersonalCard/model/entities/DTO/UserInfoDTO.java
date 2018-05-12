package com.frame.component.ui.acticity.PersonalCard.model.entities.DTO;

import com.frame.component.ui.acticity.PersonalCard.model.entities.EntitiesUtil;
import com.frame.component.ui.acticity.PersonalCard.model.entities.UserInfo;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.http.api.Mapper;

import java.util.ArrayList;
import java.util.List;

public class UserInfoDTO implements Mapper<UserInfo> {
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

    @Override
    public UserInfo transform() {
        UserInfo object = new UserInfo();

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

        return object;
    }
}
