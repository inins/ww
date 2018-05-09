package com.frame.component.entities.dto;

import com.frame.component.entities.FriendInfo;
import com.frame.component.enums.Gender;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.http.api.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-08 10:10
 * ============================================
 */
public class FriendInfoDTO implements Mapper<FriendInfo> {

    private String userId;
    private String nickname;
    private String avatar;
    private int sex;
    private int age;
    private String constellation;
    private List<TagDTO> tags;

    @Override
    public FriendInfo transform() {
        FriendInfo friend = new FriendInfo();
        friend.setFriendId(userId == null ? "-1" : userId);
        friend.setNickname(nickname == null ? "" : nickname);
        friend.setPortrait(avatar == null ? "" : avatar);
        friend.setGender(sex == 0 ? Gender.MALE : Gender.FEMALE);
        friend.setAge(age);
        friend.setConstellation(constellation);
        List<Tag> tagList = new ArrayList<>();
        if (tags != null) {
            for (TagDTO tag : tags) {
                tagList.add(tag.transform());
            }
        }
        friend.setTags(tagList);
        return friend;
    }
}
