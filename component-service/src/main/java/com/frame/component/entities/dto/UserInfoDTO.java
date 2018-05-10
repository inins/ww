package com.frame.component.entities.dto;

import com.frame.component.entities.UserInfo;
import com.frame.component.enums.Gender;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.http.api.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-10 16:21
 * ============================================
 */
public class UserInfoDTO implements Mapper<UserInfo> {

    private String userId;
    private String avatar;
    private String nickname;
    private int sex;
    private long birthday;
    private String constellation;
    private long createTime;
    private List<TagDTO> tags;

    @Override
    public UserInfo transform() {
        UserInfo user = new UserInfo();
        user.setUserId(userId == null ? "-1" : userId);
        user.setPortrait(avatar == null ? "" : avatar);
        user.setNickname(nickname == null ? "" : nickname);
        user.setGender(sex == 0 ? Gender.MALE : Gender.FEMALE);
        user.setBirthMills(birthday);
        user.setConstellation(constellation == null ? "" : constellation);
        user.setRegisterTime(createTime);
        List<Tag> tagList = new ArrayList<>();
        if (tags != null) {
            for (TagDTO tagDTO : tags) {
                tagList.add(tagDTO.transform());
            }
        }
        user.setTags(tagList);
        return user;
    }
}
